package ru.sberbank.school.task13.annotations;

import ru.sberbank.school.task13.enums.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    CacheType cacheType();

    String fileNamePrefix() default "";

    Class[] identityBy() default {};
}