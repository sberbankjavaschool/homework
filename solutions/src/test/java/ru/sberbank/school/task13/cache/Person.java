package ru.sberbank.school.task13.cache;

import java.io.Serializable;

public class Person implements IPerson, Serializable {

    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String rename(String newName, String prefix) {
        String oldName = name;
        if (!newName.equals(name)) {
            name = prefix + newName;
        }
        return oldName;
    }

    public Integer getAgeAfter(Integer years) {
        return age + years;
    }

    public Integer getAgeBefore(Integer years) {
        return age - years;
    }
}
