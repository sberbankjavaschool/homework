package ru.sberbank.school.task13.cacheproxy;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    enum Cachetype { MEMORY, FILE }

    Cachetype cacheType() default Cachetype.FILE;

    Class[] uniqueFields() default {};

    String fileNamePrefix() default "";

    boolean zip() default false;
}
