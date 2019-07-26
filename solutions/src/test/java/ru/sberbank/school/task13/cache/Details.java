package ru.sberbank.school.task13.cache;

import java.io.Serializable;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class Details implements Serializable {
    private String name;
    private Integer age;

    public Details(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Details{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
