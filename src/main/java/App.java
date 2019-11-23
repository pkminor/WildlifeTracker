import dao.Sql2oAnimalDao;
import dao.Sql2oEndangeredAnimalDao;
import dao.Sql2oSightingDao;
import models.Animal;
import models.EndangeredAnimal;
import models.Sighting;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.Timestamp;
import java.util.*;

import static spark.Spark.*;

public class App {
    private static  Sql2o sql2o;
    private static Sql2oSightingDao sightingDao;
    private static Sql2oAnimalDao animalDao;
    private static Sql2oEndangeredAnimalDao endangeredAnimalDao;

    public static void main(String[] args) {
        ProcessBuilder process = new ProcessBuilder();
        Integer port;

        // This tells our app that if Heroku sets a port for us, we need to use that port.
        // Otherwise, if they do not, continue using port 4567.

        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 7654;
        }

        port(port);

        String connectionStr="jdbc:postgresql://localhost:5432/wildlife_tracker";
        sql2o = new Sql2o(connectionStr,"pkminor","password");
        sightingDao = new Sql2oSightingDao(sql2o);
        animalDao = new Sql2oAnimalDao(sql2o);
        endangeredAnimalDao =  new Sql2oEndangeredAnimalDao(sql2o);

        staticFileLocation("/public");

        get("/", (req,res)->index(req), new HandlebarsTemplateEngine());
        get("/sighted",(req,res)->index(req), new HandlebarsTemplateEngine());
        get("/unsighted",(req,res)->index(req), new HandlebarsTemplateEngine());

        get("/animals", (req,res)->animals(req,res), new HandlebarsTemplateEngine());
        get("/animals/sighted",(req,res)->animals(req,res), new HandlebarsTemplateEngine());
        get("/animals/unsighted",(req,res)->animals(req,res), new HandlebarsTemplateEngine());

        get("/endangered", (req,res)->endangered(req,res), new HandlebarsTemplateEngine());
        get("/endangered/sighted",(req,res)->endangered(req,res), new HandlebarsTemplateEngine());
        get("/endangered/unsighted",(req,res)->endangered(req,res), new HandlebarsTemplateEngine());

        get("/sightings",(req,res)->sightings(req,res), new HandlebarsTemplateEngine());
        get("/sightings/animals", (req,res)->sightings(req,res), new HandlebarsTemplateEngine());
        get("/sightings/endangered", (req,res)->sightings(req,res), new HandlebarsTemplateEngine());


        post("/animals/new",(req,res)->{
            String animal = req.queryParams("animal");


            String route="";
            if(req.queryParams("endangered")==null){
               if(animal.length()>0) {
                   animalDao.addAnimal(new Animal(0,animal,Sql2oAnimalDao.RECORD_TYPE));
                   //update animals list
                   req.session().attribute("animals",animalDao.getAllAnimals());
                   req.session().attribute("data",req.session().attribute("animals"));
               }

                route="/animals";

            }
            else{
                String health = req.queryParams("health");
                String age = req.queryParams("age");

                if(health.length()>0 && age.length()>0 && animal.length()>0){
                    endangeredAnimalDao.addEndangeredAnimal(
                            new EndangeredAnimal(0,animal,Sql2oEndangeredAnimalDao.RECORD_TYPE,health,age));

                    //update endangered list
                    req.session().attribute("endangered",endangeredAnimalDao.getAllEndangeredAnimals());
                    req.session().attribute("data",req.session().attribute("endangered"));
                }

                route="/endangered";

            }

            List<Animal> wildlife = new ArrayList<>();
            wildlife.addAll(req.session().attribute("animals"));
            wildlife.addAll(req.session().attribute("endangered"));
            req.session().attribute("wildlife",wildlife); //update upon adding animal

            res.redirect(route);


            return null;

        },new HandlebarsTemplateEngine());

        post("/sightings/new", (req,res)->{
            int aid = Integer.parseInt(req.queryParams("animal"));
            String location = req.queryParams("location");
            String ranger = req.queryParams("rangername");

            if(aid>0 && location.length()>0 && ranger.length()>0){
                Sighting sighting = new Sighting(0,aid,location,ranger,new Timestamp(new Date().getTime()));
                sightingDao.addSighting(sighting);
            }

            res.redirect("/sightings");
            return null;

        },new HandlebarsTemplateEngine());

    }

    //helper for: /, /sighted, /unsighted
    private static ModelAndView index(Request req){

        Map<String, Object> model = new HashMap<String,Object>();

        //shield root route logic from reroutes
        if (req.session().attribute("isreroute")==null){
            updateCategoryRoutes(req);

            List<Animal> wildlife = new ArrayList<>();
            List<Animal> animals = animalDao.getAllAnimals();
            List<EndangeredAnimal> endangered = endangeredAnimalDao.getAllEndangeredAnimals();

            wildlife.addAll(animals);
            wildlife.addAll(endangered);

            //use sessions to reduce db queries
            req.session().attribute("data",
                    req.uri().equals("/sighted")? sightingDao.getSightedWildlife():
                    req.uri().equals("/unsighted")? sightingDao.getUnsightedWildlife(): wildlife
            ); //all,animals,endangered : sighted/unsighted

            req.session().attribute("animals",animals);
            req.session().attribute("endangered",endangered);
            req.session().attribute("wildlife",wildlife); //set this first time, and on adding new animal/endagered
            req.session().attribute("sightings",null);
        }

        model.put("routes",req.session().attribute("categoryRoutes"));
        model.put("data",req.session().attribute("data"));   //this to be set correctly in other category routes
        model.put("wildlife",req.session().attribute("wildlife")); //only chnages if adding new animal/endangered
        model.put("sightings",req.session().attribute("sightings"));

        //resets
        req.session().attribute("isreroute",null); //reset flag. always null unless another route changes.
        req.session().attribute("data",null); //routes to set their data
        req.session().attribute("sightings",null); //only the sightings route sets this.

        return new ModelAndView(model,"index.hbs");
    }

    // helper for: /animals, /animals/sighted, /animals/unsighted
    private static  ModelAndView animals(Request req, Response res){

        updateCategoryRoutes(req); //updates session categoryRoutes

        //updates data, defaults to all animals.
        req.session().attribute("data",
                req.uri().equals("/animals")? animalDao.getAllAnimals():
                req.uri().equals("/animals/sighted")? sightingDao.getSightedAnimals():
                req.uri().equals("/animals/unsighted")? sightingDao.getUnsightedAnimals():animalDao.getAllAnimals());

        req.session().attribute("isreroute",true); //switch flag on
        res.redirect("/");
        return null;

    }

    // helper for: /endangered, /endangered/sighted, /endangered/unsighted
    private static  ModelAndView endangered(Request req, Response res){

        updateCategoryRoutes(req); //updates session categoryRoutes

        //updates data, defaults to all endangered animals.
        req.session().attribute("data",
                req.uri().equals("/endangered")? endangeredAnimalDao.getAllEndangeredAnimals():
                        req.uri().equals("/endangered/sighted")? sightingDao.getSightedEndangeredAnimals():
                                req.uri().equals("/endangered/unsighted")? sightingDao.getUnsightedEndangeredAnimals():
                                        endangeredAnimalDao.getAllEndangeredAnimals());

        req.session().attribute("isreroute",true); //switch flag on
        res.redirect("/");
        return null;

    }

    // helper for: /sightings, /sightings/animals, sightings/endangered
    private static  ModelAndView sightings(Request req, Response res){

        updateCategoryRoutes(req); //updates session categoryRoutes

        //updates data depending on route. defaults to all wildlife
        req.session().attribute("sightings",
                req.uri().equals("/sightings/animals")? sightingDao.getAnimalSightings():
                req.uri().equals("/sightings/endangered")? sightingDao.getEndangeredAnimalSightings():
                        sightingDao.getAllSightings()

                );

        req.session().attribute("isreroute",true); //switch flag on
        res.redirect("/");
        return null;

    }

    private static void updateCategoryRoutes(Request req){

            List<CategoryRoute> routes = getCategoryRoutes();

            routes.forEach(r-> r.setActive(r.getUrl().equals(req.uri())? true:false));
            req.session().attribute("categoryRoutes", routes);

    }
    private static List<CategoryRoute> getCategoryRoutes(){
        List<CategoryRoute> categoryRouteList = new ArrayList<>();

        categoryRouteList.addAll(
                Arrays.asList(
                        new CategoryRoute("/","All Wildlife"),
                        new CategoryRoute("/sighted","Sighted Wildlife"),
                        new CategoryRoute("/unsighted","Unsighted Wildlife"),

                        new CategoryRoute("/animals","Animals"),
                        new CategoryRoute("/animals/sighted","Sighted Animals"),
                        new CategoryRoute("/animals/unsighted","Unsighted Animals"),

                        new CategoryRoute("/endangered","Endangered"),
                        new CategoryRoute("/endangered/sighted","Sighted Endangered"),
                        new CategoryRoute("/endangered/unsighted","Unsighted Endangered"),

                        new CategoryRoute("/sightings","All Sightings"),
                        new CategoryRoute("/sightings/animals","Animal Sightings"),
                        new CategoryRoute("/sightings/endangered","Endangered Sightings")

                ));
        return categoryRouteList;
    }
    private static class CategoryRoute{
        private String url;
        private String label;
        private boolean active;

        public CategoryRoute(String url, String label) {
            this.url = url;
            this.label = label;
            this.active = false;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
