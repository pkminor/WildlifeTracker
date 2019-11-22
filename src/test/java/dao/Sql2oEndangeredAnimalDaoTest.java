package dao;

import models.EndangeredAnimal;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oEndangeredAnimalDaoTest {

    private  static Sql2oEndangeredAnimalDao endangeredAnimalDao;
    private static Connection con;

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionStr="jdbc:postgresql://localhost:5432/wildlife_tracker_test";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        endangeredAnimalDao = new Sql2oEndangeredAnimalDao(sql2o);
        con = sql2o.open();
        endangeredAnimalDao.clearAllEndangeredAnimals(); //start with empty table
    }

    @After
    public void tearDown() throws Exception { endangeredAnimalDao.clearAllEndangeredAnimals(); }

    @AfterClass
    public static void shutDown() throws Exception { con.close();  }

    @Test
    public void getAllEndangeredAnimals_ReturnsAll_True() {
        EndangeredAnimal endangeredAnimal = setupEndangeredAnimal();
        EndangeredAnimal endangeredAnimal2 = setupEndangeredAnimal();

        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal);
        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal2);

        assertEquals(2,endangeredAnimalDao.getAllEndangeredAnimals().size());
        assertTrue(endangeredAnimalDao.getAllEndangeredAnimals().containsAll(Arrays.asList(endangeredAnimal,endangeredAnimal2)));

    }

    @Test
    public void addEndangeredAnimal_SetsIdAddsAll_True() {
        EndangeredAnimal endangeredAnimal = setupEndangeredAnimal();
        EndangeredAnimal endangeredAnimal2 = setupEndangeredAnimal();

        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal);
        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal2);

        assertNotEquals(0, endangeredAnimal.getId());
        assertNotEquals(0, endangeredAnimal2.getId());
        assertTrue(endangeredAnimal2.getId()> endangeredAnimal.getId());
        assertTrue(1 == endangeredAnimal2.getId() - endangeredAnimal.getId());
        assertEquals(2,endangeredAnimalDao.getAllEndangeredAnimals().size());
    }

    @Test
    public void findEndangeredAnimalById_CorectlyReturnsById_True() {
        EndangeredAnimal endangeredAnimal = setupEndangeredAnimal();
        EndangeredAnimal endangeredAnimal2 = setupEndangeredAnimal();

        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal);
        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal2);

        assertEquals(endangeredAnimal, endangeredAnimalDao.findEndangeredAnimalById(endangeredAnimal.getId()));
    }

    @Test
    public void updateEndangeredAnimal_SetsNameHealthAgeUpdates() {
        EndangeredAnimal endangeredAnimal = setupEndangeredAnimal();
        EndangeredAnimal endangeredAnimal2 = setupEndangeredAnimal();

        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal);
        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal2);

        String original_name = endangeredAnimal.getName();
        String original_health=endangeredAnimal.getHealth();
        String original_age = endangeredAnimal.getAge();

        endangeredAnimalDao.updateEndangeredAnimal(endangeredAnimal,"rhino","okay","adult");

        assertNotEquals(original_name, endangeredAnimal.getName());
        assertNotEquals(original_health, endangeredAnimal.getHealth());
        assertNotEquals(original_age, endangeredAnimal.getAge());

        assertNotEquals(original_name,endangeredAnimalDao.findEndangeredAnimalById(endangeredAnimal.getId()).getName());
        assertNotEquals(original_health,endangeredAnimalDao.findEndangeredAnimalById(endangeredAnimal.getId()).getHealth());
        assertNotEquals(original_age,endangeredAnimalDao.findEndangeredAnimalById(endangeredAnimal.getId()).getAge());

    }

    @Test
    public void clearAllEndangeredAnimals_ClearsAll() {
        EndangeredAnimal endangeredAnimal = setupEndangeredAnimal();
        EndangeredAnimal endangeredAnimal2 = setupEndangeredAnimal();

        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal);
        endangeredAnimalDao.addEndangeredAnimal(endangeredAnimal2);
        endangeredAnimalDao.clearAllEndangeredAnimals();

        assertEquals(0,endangeredAnimalDao.getAllEndangeredAnimals().size());
    }

    private EndangeredAnimal setupEndangeredAnimal(){
        return new EndangeredAnimal(0,"elephant",endangeredAnimalDao.RECORD_TYPE,"weak","old");
    }
}