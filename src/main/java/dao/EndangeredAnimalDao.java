package dao;

import models.EndangeredAnimal;

import java.util.List;

public interface EndangeredAnimalDao {

    List<EndangeredAnimal> getAllEndangeredAnimals();
    void addEndangeredAnimal(EndangeredAnimal endangeredAnimal);
    EndangeredAnimal findEndangeredAnimalById(int id);
    void updateEndangeredAnimal(EndangeredAnimal endangeredAnimal, String name, String health, String age);

    void clearAllEndangeredAnimals();
}
