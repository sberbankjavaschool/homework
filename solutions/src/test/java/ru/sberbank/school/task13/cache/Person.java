package ru.sberbank.school.task13.cache;

import java.io.Serializable;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class Person implements IPerson, Serializable {
    private String name;
    private Integer age;
    private Temp superTemp;
    private String hair;

    public Person(String name, Integer age, Temp superTemp) {
        this.name = name;
        this.age = age;
        this.superTemp = superTemp;
        this.hair = "";
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    @Override
    public String combineHair(String newHair) {
        hair += " и " + newHair;
        return hair;
    }

    @Override
    public Details getNameAndAge() {
        return new Details(name, age);
    }

    @Override
    public void setDetails(Details details) {
        this.name = details.getName();
        this.age = details.getAge();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public Temp getSuperTemp() {
        return superTemp;
    }

    @Override
    public void setSuperTemp(Temp superTemp) {
        this.superTemp = superTemp;
    }
}
