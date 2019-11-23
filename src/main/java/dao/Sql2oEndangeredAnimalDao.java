package dao;

import models.EndangeredAnimal;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oEndangeredAnimalDao implements EndangeredAnimalDao {

    private final Sql2o sql2o;
    public static final String RECORD_TYPE = "endangered";

    public enum HEALTHSTATUS {HEALTHY,ILL,OKAY};
    public enum AGESTATUS {NEWBOARD,YOUNG,ADULT};

    public Sql2oEndangeredAnimalDao(Sql2o sql2o) {
        this.sql2o=sql2o;
    }

    @Override
    public List<EndangeredAnimal> getAllEndangeredAnimals() {
        String sql ="select * from animals where type=:type";
        try(Connection con =  sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("type",RECORD_TYPE)
                    .executeAndFetch(EndangeredAnimal.class);
        }
    }

    @Override
    public void addEndangeredAnimal(EndangeredAnimal endangeredAnimal) {
        String sql ="insert into animals (name, health, age,type) values (:name,:health,:age,:type)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql,true)
                    .bind(endangeredAnimal)
                    .executeUpdate()
                    .getKey();
            endangeredAnimal.setId(id);
        }
    }

    @Override
    public EndangeredAnimal findEndangeredAnimalById(int id) {
        String sql ="select * from animals where id= :id ";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(EndangeredAnimal.class);
        }
    }

    @Override
    public void updateEndangeredAnimal(EndangeredAnimal endangeredAnimal, String name, String health, String age) {
        String sql ="update animals set (name, health, age) = (:name,:health,:age) where id= :id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name",name)
                    .addParameter("health",health)
                    .addParameter("age",age)
                    .addParameter("id",endangeredAnimal.getId())
                    .executeUpdate();

            endangeredAnimal.setName(name);
            endangeredAnimal.setHealth(health);
            endangeredAnimal.setAge(age);
        }

    }

    @Override
    public void clearAllEndangeredAnimals() {
        String sql ="delete from animals where type=:type";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("type",RECORD_TYPE)
                    .executeUpdate();
        }
    }
}
