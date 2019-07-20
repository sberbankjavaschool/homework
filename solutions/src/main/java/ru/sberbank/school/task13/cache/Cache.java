package ru.sberbank.school.task13.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    CacheType cacheType() default CacheType.JVM;
    String serviceName() default "";
    boolean zip() default false;
    Class[] identityBy() default {};
}
