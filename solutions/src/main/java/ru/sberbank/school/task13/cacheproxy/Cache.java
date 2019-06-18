package ru.sberbank.school.task13.cacheproxy;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    enum cacheType {MEMORY, FILE}

    cacheType cacheType() default cacheType.FILE;

    Class[] uniqueFields() default {};

    String fileNamePrefix() default "";

    boolean zip() default false;
}
