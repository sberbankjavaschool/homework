package ru.sberbank.school.task06;

import java.util.Objects;

public class Pet {
    private String name;
    private boolean sex;
    private Integer age;
    private String breed;

    public Pet(String name, boolean sex, int age, String breed) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pet pet = (Pet) o;
        return sex == pet.sex
                && age == pet.age
                && Objects.equals(name, pet.name)
                && Objects.equals(breed, pet.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex, age, breed);
    }


}
