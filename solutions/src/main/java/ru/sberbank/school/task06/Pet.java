package ru.sberbank.school.task06;

import lombok.*;

@Getter
@ToString
public class Pet implements Comparable<Pet> {

    private String name;
    private int age;

    public Pet() {

    }

    public Pet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Pet o) {
        if (age > o.getAge()) {
            return 1;
        }
        if (age < o.getAge()) {
            return -1;
        }

        return 0;
    }
}
