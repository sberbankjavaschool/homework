package ru.sberbank.school.task13.cacheproxy.services;

import ru.sberbank.school.task13.cacheproxy.annotation.Cache;
import ru.sberbank.school.task13.cacheproxy.annotation.CacheType;

import java.util.List;

public interface CacheTest {

    @Cache(getCacheType = CacheType.MEMORY)
    String getResultUseMemoryCache();

    @Cache(getCacheType = CacheType.FILE, key = "cache_type")
    String getResultUseFileCache();

    @Cache(getCacheType = CacheType.FILE, zip = true)
    void zipFileTest();

    @Cache(getCacheType = CacheType.MEMORY, listSize = 100)
    List createList(int size);

    @Cache(getCacheType = CacheType.MEMORY, identityBy = {String.class})
    int stringSize(String str, int useless);

    int getCallCounter();
}
