package dao;

import models.Animal;

import java.util.List;

public interface AnimalDao {

    List<Animal> getAllAnimals();
    void addAnimal(Animal animal);
    Animal findAnimalById(int id);
    void updateAnimal(Animal animal, String name);

    void clearAllAnimals();
}
