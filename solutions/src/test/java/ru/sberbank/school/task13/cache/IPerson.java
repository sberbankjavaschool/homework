package ru.sberbank.school.task13.cache;

import ru.sberbank.school.task13.cache.annotation.Cache;
import ru.sberbank.school.task13.cache.annotation.CacheType;

public interface IPerson {
    @Cache(cacheType = CacheType.JVM)
    String getName();

    @Cache(cacheType = CacheType.JVM)
    void setName(String name);

    Integer getAge();

    void setAge(Integer age);

    @Cache(cacheType = CacheType.FILE, indexIdentity = {0})
    String rename(String newName, String prefix);

    @Cache(cacheType = CacheType.JVM, key = "TestGetAgeAfter")
    Integer getAgeAfter(Integer years);

    Integer getAgeBefore(Integer years);
}
