package ru.sberbank.school.task13.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    CachingType cacheType() default CachingType.RAM;
    String name() default "";
    //TODO имя по ключу + выбор параметров
    String path() default "";
    Class[] params() default {Object.class};
}
