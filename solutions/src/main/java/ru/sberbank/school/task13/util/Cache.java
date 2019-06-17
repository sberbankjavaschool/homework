package ru.sberbank.school.task13.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {

    CacheType cacheType();

    String fileNameOrKey();

    boolean zip();

    Class[] identityBy();
}
