package ru.sberbank.school.task06;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Mart
 * 31.05.2019
 **/
@AllArgsConstructor
@Data
public class Employee {
    private int id;
    private int age;
    private String name;

    public Employee(Employee other) {
        this.id = other.id;
        this.age = other.age;
        this.name = other.name;
    }
}
