package ru.sberbank.school.task13.cacheproxy;

import ru.sberbank.school.task13.cacheproxy.annotation.Cache;
import ru.sberbank.school.task13.cacheproxy.annotation.CacheType;

public interface ServiceTestInterface {
    @Cache(cacheType = CacheType.JVM)
    String cacheInJvm(String arg);

    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "name")
    Integer cacheInFile(int arg1, String arg2);

    @Cache(cacheType = CacheType.JVM, identityBy = {Integer.class})
    Integer sumWithPropertyIdentityByFirstArgument(int arg1, int arg2);

    @Cache(cacheType = CacheType.FILE, identityBy = {Integer.class})
    String sumStringWithPropertyIdentityByFirstArgument(int arg1, String arg2, String arg3);
}
