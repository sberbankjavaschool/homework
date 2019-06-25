package ru.sberbank.school.task13.copier;

public class Person {

    private String name;
    private Byte age;
    private Integer weight;
    private Pet pet;

    public Person(String name, Byte age, Integer weight, Pet pet) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.pet = pet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    protected Integer getWeight() {
        return weight;
    }

    protected void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Pet getAnimal() {
        return pet;
    }

    public void setAnimal(Pet pet) {
        this.pet = pet;
    }
}
