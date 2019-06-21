package ru.sberbank.school.task13.beanfieldcopier.testclasses;

public class Person {
    private String name;
    private String surname;
    private int age;
    private int weight;

    public Person(String name, String surname, int age, int weight) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.weight = weight;
    }

    public Person() { }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }
}
