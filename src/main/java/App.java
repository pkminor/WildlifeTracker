import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
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

        staticFileLocation("/public");

        get("/", (req,res)->index(), new HandlebarsTemplateEngine());

    }

    private static ModelAndView index(){
        Map<String, Object> model = new HashMap<String,Object>();

        return new ModelAndView(model,"index.hbs");
    }
}
