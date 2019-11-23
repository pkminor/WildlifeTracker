package dao;

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
    private static Connection con;

    @BeforeClass
    public static void setUp() throws Exception {
        String connectionStr="jdbc:postgresql://localhost:5432/wildlife_tracker_test";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        sightingDao = new Sql2oSightingDao(sql2o);
        con = sql2o.open();
        sightingDao.clearAllSightings(); //start with empty table
    }

    @After
    public void tearDown() throws Exception { sightingDao.clearAllSightings();   }

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

    @Test
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