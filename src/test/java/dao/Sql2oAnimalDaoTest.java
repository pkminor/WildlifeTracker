package dao;

import models.Animal;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oAnimalDaoTest {

    private  static Sql2oAnimalDao animalDao;
    private static Connection con;

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionStr="jdbc:postgresql://localhost:5432/wildlife_tracker_test";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        animalDao = new Sql2oAnimalDao(sql2o);
        con = sql2o.open();
        animalDao.clearAllAnimals(); //start with empty table
    }

    @After
    public void tearDown() throws Exception {
        animalDao.clearAllAnimals(); //clear table after each test
    }

    @AfterClass
    public static void shutDown() throws Exception { con.close(); }

    @Test
    public void getAllAnimals_ReturnsAllAnimals_True() {

        Animal animal = setupAnimal();
        Animal animal2 = setupAnimal();

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal2);

        assertEquals(2,animalDao.getAllAnimals().size() );
        assertTrue(animalDao.getAllAnimals().containsAll(Arrays.asList(animal,animal2)));

    }

    @Test
    public void addAnimal_SetsIdAddsAllAnimals_True() {
        Animal animal = setupAnimal();
        Animal animal2 = setupAnimal();

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal2);

        assertTrue( 0!=animal.getId() );
        assertTrue( 0!=animal2.getId());
        assertTrue(animal2.getId() >  animal.getId());
        assertTrue( 1==animal2.getId()-animal.getId());
        assertTrue( 2== animalDao.getAllAnimals().size());

    }

    @Test
    public void findAnimalById_ReturnsCorrectAnimal_Animal() {
        Animal animal = setupAnimal();
        Animal animal2 = setupAnimal();

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal2);

        assertEquals(animal, animalDao.findAnimalById(animal.getId()));
    }

    @Test
    public void updateAnimal_UpdatesName_True() {
        Animal animal = setupAnimal();
        Animal animal2 = setupAnimal();

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal2);

        String original_name = animal.getName();
        animalDao.updateAnimal(animal,"leopard");

        assertNotEquals(original_name, animal.getName());
        assertNotEquals(original_name, animalDao.findAnimalById(animal.getId()).getName());
    }

    @Test
    public void clearAllAnimals_ClearsAll_True() {
        Animal animal = setupAnimal();
        Animal animal2 = setupAnimal();

        animalDao.addAnimal(animal);
        animalDao.addAnimal(animal2);
        animalDao.clearAllAnimals();
        assertEquals(0,animalDao.getAllAnimals().size());
    }

    private Animal setupAnimal(){return new Animal(0,"lion",animalDao.RECORD_TYPE);}
}