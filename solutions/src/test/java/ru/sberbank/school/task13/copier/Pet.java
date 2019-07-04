package ru.sberbank.school.task13.copier;

public class Pet extends Animal {

    private String name;
    private String breed;
    private boolean fluffy;

    Pet(String name, Integer age, Integer weight, String breed, boolean fluffy) {
        super(name + age ,age, weight);
        this.name = name;
        this.breed = breed;
        this.fluffy = fluffy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public boolean isFluffy() {
        return fluffy;
    }

    public void setFluffy(boolean fluffy) {
        this.fluffy = fluffy;
    }
}
