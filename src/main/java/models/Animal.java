package models;

import java.util.Comparator;
import java.util.Objects;

public class Animal implements Comparator<Animal>,  Comparable<Animal> {
    private int id;
    private String name;
    private String type;

    public Animal(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compare(Animal animal, Animal t1) {
        return animal.id - t1.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id &&
                Objects.equals(name, animal.name) &&
                Objects.equals(type, animal.type);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public int compareTo(Animal animal) {
        return this.getId() - animal.getId();
    }
}
