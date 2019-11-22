package models;

import java.util.Objects;

public class EndangeredAnimal extends  Animal {
    private String health;
    private String age;

    public EndangeredAnimal(int id, String name, String type, String health, String age) {
        super(id, name, type);
        this.health = health;
        this.age = age;
    }

    public String getHealth() {
        return health;
    }
    public void setHealth(String health) {
        this.health = health;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EndangeredAnimal that = (EndangeredAnimal) o;
        return Objects.equals(health, that.health) &&
                Objects.equals(age, that.age) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getName(), that.getName()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), health, age);
    }
}