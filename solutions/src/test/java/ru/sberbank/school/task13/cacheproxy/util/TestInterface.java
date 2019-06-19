package ru.sberbank.school.task13.cacheproxy.util;

import ru.sberbank.school.task13.cacheproxy.util.Cache;

public interface TestInterface {

    @Cache
    void voidMethod();

    @Cache
    String getResultUseMemoryCache();

    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "data_", identityBy = {String.class, double.class})
    String getResultUseFileCache(String str, double dbl, int delta);

}
