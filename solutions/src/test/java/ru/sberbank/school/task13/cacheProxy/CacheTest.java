package ru.sberbank.school.task13.cacheProxy;

import ru.sberbank.school.task13.cacheProxy.annotation.Cache;
import ru.sberbank.school.task13.cacheProxy.annotation.CacheType;

import java.util.List;

public interface CacheTest {

    @Cache(getCacheType = CacheType.JVM)
    String cacheInJVM();

    @Cache(getCacheType = CacheType.FILE, key = "cache_type")
    String cacheInFile();

    @Cache(getCacheType = CacheType.FILE, zip = true)
    void cacheInZip();

    @Cache(getCacheType = CacheType.JVM, listSize = 50)
    List getList(int size);

    @Cache(getCacheType = CacheType.JVM, identityBy = {String.class})
    int getStringSize(String s, int i);

    int getCount();
}