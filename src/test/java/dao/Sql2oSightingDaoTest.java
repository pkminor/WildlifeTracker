package dao;

import models.Animal;
import models.EndangeredAnimal;
import models.Sighting;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class Sql2oSightingDaoTest {

    private  static Sql2oSightingDao sightingDao;
    private  static  Sql2oAnimalDao animalDao;
    private static Sql2oEndangeredAnimalDao endangeredAnimalDao;
    private static Connection con;

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionStr="jdbc:postgresql://localhost:5432/wildlife_tracker_test";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        sightingDao = new Sql2oSightingDao(sql2o);
        animalDao = new Sql2oAnimalDao(sql2o);
        endangeredAnimalDao =  new Sql2oEndangeredAnimalDao(sql2o);

        con = sql2o.open();
        sightingDao.clearAllSightings(); //start with empty table
        animalDao.clearAllAnimals();
        endangeredAnimalDao.clearAllEndangeredAnimals();
    }

    @After
    public void tearDown() throws Exception {
        sightingDao.clearAllSightings();
        animalDao.clearAllAnimals();
        endangeredAnimalDao.clearAllEndangeredAnimals();
    }

    @AfterClass
    public static void shutDown() throws Exception { con.close(); }

    @Test
    public void getAllSightings_ReturnsAll_True() {
        Sighting s1 = setupSighting();
        Sighting s2 = setupSighting();

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertEquals(2, sightingDao.getAllSightings().size());
        assertTrue(sightingDao.getAllSightings().containsAll(Arrays.asList(s1,s2)));
    }

    @Test
    public void getSightedAnimals_ReturnsOnlySightedAnimals_True() {
        Animal a1 = new Animal(1,"lion",animalDao.RECORD_TYPE);
        Animal a2 = new Animal(2,"ostrich",animalDao.RECORD_TYPE);
        animalDao.addAnimal(a1);
        animalDao.addAnimal(a2);

        EndangeredAnimal ea1 = new EndangeredAnimal(1,"elephant",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.OKAY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.ADULT.toString());

        EndangeredAnimal ea2 = new EndangeredAnimal(2,"rhino",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.HEALTHY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.YOUNG.toString());

        endangeredAnimalDao.addEndangeredAnimal(ea1);
        endangeredAnimalDao.addEndangeredAnimal(ea2);

        Sighting s1 = new Sighting(1,a1.getId(),"river","ryan",new Timestamp(new Date().getTime()));
        Sighting s2 = new Sighting(2,ea1.getId(),"ridge","ryan",new Timestamp(new Date().getTime()));

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertTrue(sightingDao.getSightedAnimals().contains(a1)); //animal sighted
        assertFalse(sightingDao.getSightedAnimals().contains(a2)); //animal not sighted
        assertFalse(sightingDao.getSightedAnimals().contains(ea1)); //sighted but not an animal object
        assertFalse(sightingDao.getSightedAnimals().contains(ea2)); //not sighted and not an animal object
    }

    @Test
    public void getUnsightedAnimals_ReturnsOnlyUnsightedAnimals_True() {
        Animal a1 = new Animal(1,"lion",animalDao.RECORD_TYPE);
        Animal a2 = new Animal(2,"ostrich",animalDao.RECORD_TYPE);
        animalDao.addAnimal(a1);
        animalDao.addAnimal(a2);

        EndangeredAnimal ea1 = new EndangeredAnimal(1,"elephant",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.OKAY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.ADULT.toString());

        EndangeredAnimal ea2 = new EndangeredAnimal(2,"rhino",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.HEALTHY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.YOUNG.toString());

        endangeredAnimalDao.addEndangeredAnimal(ea1);
        endangeredAnimalDao.addEndangeredAnimal(ea2);

        Sighting s1 = new Sighting(1,a1.getId(),"river","ryan",new Timestamp(new Date().getTime()));
        Sighting s2 = new Sighting(2,ea1.getId(),"ridge","ryan",new Timestamp(new Date().getTime()));

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertFalse(sightingDao.getUnsightedAnimals().contains(a1)); //animal sighted
        assertTrue(sightingDao.getUnsightedAnimals().contains(a2)); //animal not sighted
        assertFalse(sightingDao.getUnsightedAnimals().contains(ea1)); //sighted not an animal object
        assertFalse(sightingDao.getUnsightedAnimals().contains(ea2)); //not sighted but and not an animal object
    }

    @Test
    public void getSightedEndangeredAnimals_ReturnsOnlySightedEndangeredAnimals_True() {
        Animal a1 = new Animal(1,"lion",animalDao.RECORD_TYPE);
        Animal a2 = new Animal(2,"ostrich",animalDao.RECORD_TYPE);
        animalDao.addAnimal(a1);
        animalDao.addAnimal(a2);

        EndangeredAnimal ea1 = new EndangeredAnimal(1,"elephant",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.OKAY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.ADULT.toString());

        EndangeredAnimal ea2 = new EndangeredAnimal(2,"rhino",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.HEALTHY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.YOUNG.toString());

        endangeredAnimalDao.addEndangeredAnimal(ea1);
        endangeredAnimalDao.addEndangeredAnimal(ea2);

        Sighting s1 = new Sighting(1,a1.getId(),"river","ryan",new Timestamp(new Date().getTime()));
        Sighting s2 = new Sighting(2,ea1.getId(),"ridge","ryan",new Timestamp(new Date().getTime()));

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertFalse(sightingDao.getSightedEndangeredAnimals().contains(a1)); //not endangered animal, sighted
        assertFalse(sightingDao.getSightedEndangeredAnimals().contains(a2)); //not endangered animal, not sighted
        assertTrue(sightingDao.getSightedEndangeredAnimals().contains(ea1)); //sighted, endangered animal object
        assertFalse(sightingDao.getSightedEndangeredAnimals().contains(ea2)); //not sighted, endangered animal object
    }

    @Test
    public void getUnsightedEndangeredAnimals_ReturnsOnlyUnsightedEndangeredAnimals_True() {

        Animal a1 = new Animal(1,"lion",animalDao.RECORD_TYPE);
        Animal a2 = new Animal(2,"ostrich",animalDao.RECORD_TYPE);
        animalDao.addAnimal(a1);
        animalDao.addAnimal(a2);

        EndangeredAnimal ea1 = new EndangeredAnimal(1,"elephant",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.OKAY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.ADULT.toString());

        EndangeredAnimal ea2 = new EndangeredAnimal(2,"rhino",
                endangeredAnimalDao.RECORD_TYPE,
                Sql2oEndangeredAnimalDao.HEALTHSTATUS.HEALTHY.toString(), Sql2oEndangeredAnimalDao.AGESTATUS.YOUNG.toString());

        endangeredAnimalDao.addEndangeredAnimal(ea1);
        endangeredAnimalDao.addEndangeredAnimal(ea2);

        Sighting s1 = new Sighting(1,a1.getId(),"river","ryan",new Timestamp(new Date().getTime()));
        Sighting s2 = new Sighting(2,ea1.getId(),"ridge","ryan",new Timestamp(new Date().getTime()));

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertFalse(sightingDao.getUnsightedEndangeredAnimals().contains(a1)); //not endangered animal, sighted
        assertFalse(sightingDao.getUnsightedEndangeredAnimals().contains(a2)); //not endangered animal, not sighted
        assertFalse(sightingDao.getUnsightedEndangeredAnimals().contains(ea1)); //sighted, endangered animal object
        assertTrue(sightingDao.getUnsightedEndangeredAnimals().contains(ea2)); //not sighted, endangered animal object

    }

    @Test
    public void addSighting_SetsIdAddsAll_True() {
        Sighting s1 = setupSighting();
        Sighting s2 = setupSighting();

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        assertNotEquals(0, s1.getId());
        assertNotEquals(0, s2.getId());
        assertTrue(s2.getId() >  s1.getId());
        assertTrue(1 == s2.getId() -  s1.getId());
        assertEquals(2, sightingDao.getAllSightings().size());
    }

    @Test //(expected = IllegalArgumentException.class) //-> this is causing issues????
    public void findSightingById_FindsCorrectSighting_True() {
        Sighting s1 = setupSighting();
        Sighting s2 = setupSighting();

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        Sighting found = null;

        try {
            found = sightingDao.findSightingById(s1.getId());
        }catch (IllegalArgumentException ex){ System.out.println(ex);}

        assertEquals(s1, found);

    }

    @Test //(expected = IllegalArgumentException.class) //-> this is causing issues????
    public void updateSighting_SetsAidLocationRangernameUpdates() {
        Sighting s1 = setupSighting();
        Sighting s2 = setupSighting();

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        int original_aid = s1.getAid();
        String original_location=s1.getLocation();
        String original_rangername=s1.getRangername();

        try {
            sightingDao.updateSighting(s1,3,"ridge","Ann");
        }catch(IllegalArgumentException ex){ System.out.println(ex);}

        assertNotEquals(original_aid, s1.getAid());
        assertNotEquals(original_location, s1.getLocation());
        assertNotEquals(original_rangername, s1.getRangername());

        assertNotEquals(original_aid, sightingDao.findSightingById(s1.getId()).getAid());
        assertNotEquals(original_location, sightingDao.findSightingById(s1.getId()).getLocation());
        assertNotEquals(original_rangername, sightingDao.findSightingById(s1.getId()).getRangername());


    }

    @Test
    public void clearAllSightings_ClearsAll_True() {
        Sighting s1 = setupSighting();
        Sighting s2 = setupSighting();

        sightingDao.addSighting(s1);
        sightingDao.addSighting(s2);

        sightingDao.clearAllSightings();

        assertEquals(0, sightingDao.getAllSightings().size());

    }

    private Sighting setupSighting(){
        return new Sighting(0,1,"river","ryan",new Timestamp(new Date().getTime()));
    }
}