package ru.sberbank.school.task13;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class Human {
    private int age;
    private String name;
    private String type;

    public Human(int age, String name, String type) {
        this.age = age;
        this.name = name;
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Human{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
