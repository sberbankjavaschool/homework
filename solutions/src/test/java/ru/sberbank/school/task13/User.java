package ru.sberbank.school.task13;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class User implements IUser, Serializable {
    private String name;
    private int age;

    @Override
    public User getUserFromFile(int age, String name) {
        return new User(name, age);
    }

    @Override
    public String rename(IUser newName) {
        if (!newName.getName().equals(name)) {
            this.name = newName.getName();
        }

        return name;
    }

    @Override
    public User getUserFromMemory(int age, String name) {
        return new User(name, age);
    }
}