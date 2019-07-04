package ru.sberbank.school.task13.cacheproxy.util;

import java.util.Date;

public class TestClass {

    @Cache
    public void voidMethod() {
        System.out.println("я не кэшируюсь");
    }

    @Cache
    public String getResultUseMemoryCache() {
        return new Date().toString();
    }

    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "data_", identityBy = {String.class, double.class})
    public String getResultUseFileCache(String str, double dbl, int delta) {
        return str + dbl + delta;
    }
}
