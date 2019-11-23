package models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Sighting {

    private int id;
    private int aid;
    private String location;
    private String rangername;
    private Timestamp sightdate;

    public Sighting(int id, int aid, String location, String rangername, Timestamp sightdate) {
        this.id = id;
        this.aid = aid;
        this.location = location;
        this.rangername = rangername;
        this.sightdate = sightdate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAid() {
        return aid;
    }
    public void setAid(int aid) {
        this.aid = aid;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getRangername() {
        return rangername;
    }
    public void setRangername(String rangername) {
        this.rangername = rangername;
    }
    public String getSightdate() {
        return new SimpleDateFormat("dd MMMM yyyy").format(sightdate);
    }
    public void setSightdate(Timestamp sightdate) {
        this.sightdate = sightdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sighting sighting = (Sighting) o;
        return id == sighting.id &&
                aid == sighting.aid &&
                Objects.equals(location, sighting.location) &&
                Objects.equals(rangername, sighting.rangername) &&
                Objects.equals(sightdate, sighting.sightdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aid, location, rangername, sightdate);
    }
}
