package dao;

import models.Animal;
import models.EndangeredAnimal;
import models.Sighting;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sql2oSightingDao implements SightingDao {

    private final Sql2o sql2o;
    private final Sql2oAnimalDao animalDao;
    private final Sql2oEndangeredAnimalDao endangeredAnimalDao;
    public Sql2oSightingDao(Sql2o sql2o) {
        this.sql2o = sql2o;
        animalDao  = new Sql2oAnimalDao(sql2o);
        endangeredAnimalDao= new Sql2oEndangeredAnimalDao(sql2o);
    }

    @Override
    public List<Sighting> getAllSightings() {
        String sql ="select * from sightings";
        try(Connection con  = sql2o.open()){
            return con.createQuery(sql).executeAndFetch(Sighting.class);
        }
    }

    @Override
    public List<Sighting> getAnimalSightings() {
        List<Animal> animals = animalDao.getAllAnimals();
        return getAllSightings().stream()
                .filter(s->animals.contains(animalDao.findAnimalById(s.getAid())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Sighting> getEndangeredAnimalSightings() {
        List<EndangeredAnimal> endangeredAnimals = endangeredAnimalDao.getAllEndangeredAnimals();
        return getAllSightings().stream()
                .filter(s->endangeredAnimals.contains(endangeredAnimalDao.findEndangeredAnimalById(s.getAid())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Animal> getAllWildlife() {
        List<Animal> wildlife = new ArrayList<>();
        wildlife.addAll(animalDao.getAllAnimals());
        wildlife.addAll(endangeredAnimalDao.getAllEndangeredAnimals());

        return wildlife;
    }

    @Override
    public List<Animal> getSightedWildlife() {
        List<Animal> sightedWildlife = new ArrayList<>();
        sightedWildlife.addAll(getSightedAnimals());
        sightedWildlife.addAll(getSightedEndangeredAnimals());

        return sightedWildlife;
    }

    @Override
    public List<Animal> getUnsightedWildlife() {
        List<Animal> unSightedWildlife = new ArrayList<>();
        unSightedWildlife.addAll(getUnsightedAnimals());
        unSightedWildlife.addAll(getUnsightedEndangeredAnimals());

        return unSightedWildlife;
    }

    @Override
    public List<Animal> getSightedAnimals() {

         //sightings have animals and endangered animal objects. animalDao will only find animal objects
        //retain only animal object sightings before mapping with animalDao
        return getAnimalSightings().stream()
                .map(s -> animalDao.findAnimalById(s.getAid()))
                .collect(Collectors.toList());

        //all animals, filter to retain those in sightedAnimals
        //BAD MOVE - recursive call
        /*return animalDao.getAllAnimals().stream()
                .filter(a-> getSightedAnimals().contains(a))
                .collect(Collectors.toList());

         */
    }

    @Override
    public List<Animal> getUnsightedAnimals() {

        //all animals, filter to retain those NOT in sightedAnimals
        return animalDao.getAllAnimals().stream()
                .filter(a-> getSightedAnimals().contains(a)==false)
                .collect(Collectors.toList());
    }

    @Override
    public List<EndangeredAnimal> getSightedEndangeredAnimals() {

        //sightings have animals and endangered animal objects. endangeredAnimalDao will only find endangered animal objects
        //retain only endangered animal object sightings before mapping with endangeredAnimalDao
        return getEndangeredAnimalSightings().stream()
                .map(s -> endangeredAnimalDao.findEndangeredAnimalById(s.getAid()))
                .collect(Collectors.toList());

        //all endangered animals, filter to retain those in sightedEndangeredAnimals
        //BAD MOVE - recursive call
        /*return endangeredAnimalDao.getAllEndangeredAnimals().stream()
                .filter(a-> getSightedEndangeredAnimals().contains(a))
                .collect(Collectors.toList());

         */
    }

    @Override
    public List<EndangeredAnimal> getUnsightedEndangeredAnimals() {

        //all endangered animals, filter to retain those NOT in sightedEndangeredAnimals
        return endangeredAnimalDao.getAllEndangeredAnimals().stream()
                .filter(a-> getSightedEndangeredAnimals().contains(a)==false)
                .collect(Collectors.toList());
    }

    @Override
    public void addSighting(Sighting sighting) {
        String sql = "insert into sightings (aid,location,rangername,sightdate) values (:aid, :location, :rangername, now())";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql,true)
                    //.bind(sighting)
                    .addParameter("aid",sighting.getAid())
                    .addParameter("location",sighting.getLocation())
                    .addParameter("rangername",sighting.getRangername())
                    .executeUpdate()
                    .getKey();

            sighting.setId(id);
        }
    }

    @Override
    public Sighting findSightingById(int id) throws IllegalArgumentException {
        if(id <= 0){
            throw new IllegalArgumentException("Sighting id provided -("+id+")-is not allowed. Should be more than zero");
        }else{
            String sql ="select * from sightings where id = :id ";
            try(Connection con = sql2o.open()){
                return con.createQuery(sql)
                        .addParameter("id",id)
                        .executeAndFetchFirst(Sighting.class);
            }
        }
    }

    @Override
    public void updateSighting(Sighting sighting, int aid, String location, String rangername) throws IllegalArgumentException {

        if( aid <= 0 || location.equals("") || rangername.equals("")){
            String err = aid<=0 ? " /aid ("+aid+") " : "";
            err += location.equals("") ? " /location ("+location+") " : "";
            err += rangername.equals("") ? " /rangername ("+rangername+") ": "";
            throw new IllegalArgumentException("Arguments: " +err +", provided are not allowed. Violates rules: aid>0, location!='', rangername!=''");
        }else{
            String sql ="update sightings set (aid,location,rangername) = (:aid,:location,:rangername)  where id = :id";
            try(Connection con = sql2o.open()){
                con.createQuery(sql)
                        .addParameter("aid",aid)
                        .addParameter("location",location)
                        .addParameter("rangername",rangername)
                        .addParameter("id",sighting.getId())
                        .executeUpdate();

                sighting.setAid(aid);
                sighting.setLocation(location);
                sighting.setRangername(rangername);
            }
        }

    }

    @Override
    public void clearAllSightings() {
        String sql="delete from sightings";
        try(Connection con = sql2o.open()){
            con.createQuery(sql).executeUpdate();
        }
    }
}
