package ru.sberbank.school.task13.beanfieldcopier.testclasses;

public class AnotherPerson {
    private String name;
    private int height;
    private int weight;

    public AnotherPerson(String name, int tall, int weight) {
        this.name = name;
        this.height = tall;
        this.weight = weight;
    }

    public AnotherPerson() {}

    public String getName() {
        return name;
    }

    public int getTall() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTall(int tall) {
        this.height = tall;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
