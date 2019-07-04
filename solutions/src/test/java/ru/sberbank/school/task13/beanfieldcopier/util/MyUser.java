package ru.sberbank.school.task13.beanfieldcopier.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyUser {
    protected String name;
    protected Integer age;
    protected boolean ready;
    protected MyUser parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUser)) {
            return false;
        }
        MyUser myUser = (MyUser) o;
        return age.equals(myUser.age)
                && Objects.equals(name, myUser.name)
                && Objects.equals(parent, myUser.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, parent);
    }
}
