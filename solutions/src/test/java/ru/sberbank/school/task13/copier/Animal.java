package ru.sberbank.school.task13.copier;

public class Animal {

    private String code;
    private Integer age;
    private Integer weight;

    Animal(String code, Integer age, Integer weight) {
        this.code = code;
        this.age = age;
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
