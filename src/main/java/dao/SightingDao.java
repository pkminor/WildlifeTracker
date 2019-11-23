package dao;

import models.Sighting;

import java.util.List;

public interface SightingDao {
    List<Sighting> getAllSightings();
    void addSighting(Sighting sighting);
    Sighting findSightingById(int id);
    void updateSighting(Sighting sighting, int aid, String location, String rangername);

    void clearAllSightings();
}
