package dao;

import models.Animal;
import models.EndangeredAnimal;
import models.Sighting;

import java.util.List;

public interface SightingDao {
    List<Sighting> getAllSightings();
    List<Sighting> getAnimalSightings();
    List<Sighting> getEndangeredAnimalSightings();

    List<Animal> getAllWildlife();
    List<Animal> getSightedWildlife();
    List<Animal> getUnsightedWildlife();

    List<Animal> getSightedAnimals();
    List<Animal> getUnsightedAnimals();

    List<EndangeredAnimal> getSightedEndangeredAnimals();
    List<EndangeredAnimal> getUnsightedEndangeredAnimals();

    void addSighting(Sighting sighting);
    Sighting findSightingById(int id);
    void updateSighting(Sighting sighting, int aid, String location, String rangername);

    void clearAllSightings();
}
