package ru.sberbank.school.task13.beanfieldcopier.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MyAnotherUser {

    protected String name;
    protected Integer age;
    protected boolean ready;
    protected String parent;

}
