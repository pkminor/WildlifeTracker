package dao;

import models.Animal;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oAnimalDao implements  AnimalDao{

    private final Sql2o sql2o;
    public Sql2oAnimalDao(Sql2o sql2o) {
        this.sql2o=sql2o;
    }

    @Override
    public List<Animal> getAllAnimals() {
        String sql ="select * from animals where type='animal' ";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(Animal.class);
        }
    }

    @Override
    public void addAnimal(Animal animal) {
        String sql = "insert into animals (name,type) values (:name, :type)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql,true)
                    .addParameter("name",animal.getName())
                    .addParameter("type",animal.getType())
                    .executeUpdate()
                    .getKey();
            animal.setId(id);
        }

    }

    @Override
    public Animal findAnimalById(int id) {
        String sql = "select * from animals where id = :id";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .throwOnMappingFailure(false)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Animal.class);
        }
    }

    @Override
    public void updateAnimal(Animal animal, String name) {
        String sql ="update animals set name = :name where id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name",name)
                    .addParameter("id",animal.getId())
                    .executeUpdate();
            animal.setName(name);
        }
    }

    @Override
    public void clearAllAnimals() {
        String sql = "delete from animals where type='animal'";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
               .executeUpdate();
        }
    }
}
