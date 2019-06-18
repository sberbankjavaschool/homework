package ru.sberbank.school.task13;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachingPolicy {
    CacheType cacheType() default CacheType.MEMORY;
    String fileNamePrefix() default "";
    Class[] identityBy() default Object.class;
}
