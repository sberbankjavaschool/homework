package ru.sberbank.school.task13.copier;

public class AnimalTrainer {

    private String name;
    private Byte age;
    private Integer weight;
    private Animal animal;

    public AnimalTrainer(String name, Byte age, Integer weight, Animal animal) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.animal = animal;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
