package ru.sberbank.school.task13.cacheproxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    CacheType getCacheType() default CacheType.MEMORY;
    String key() default "";
    Class[] identityBy() default {};
    int listSize() default -1;
    boolean zip() default false;
}