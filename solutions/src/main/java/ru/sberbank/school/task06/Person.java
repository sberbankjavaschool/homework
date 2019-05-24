package ru.sberbank.school.task06;

import lombok.*;


@EqualsAndHashCode
@AllArgsConstructor
@Getter
@ToString
class Person {
    private int age;
    private String name;
    private long phoneNumber;

    public Person(Person other) {
        this.age = other.age;
        this.name = other.name;
        this.phoneNumber = other.phoneNumber;
    }

}
