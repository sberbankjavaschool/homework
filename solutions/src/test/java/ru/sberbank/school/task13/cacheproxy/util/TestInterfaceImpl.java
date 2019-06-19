package ru.sberbank.school.task13.cacheproxy.util;

import ru.sberbank.school.task13.cacheproxy.util.TestInterface;

import java.util.Date;

public class TestInterfaceImpl implements TestInterface {


    @Cache
    public String getResultFromNotOverridedMethod() {
        return "my simple String" + new Date();
    }

    @Override
    public void voidMethod() {
        System.out.println("я не кэшируюсь");
    }

    @Override
    public String getResultUseMemoryCache() {
        return new Date().toString();
    }

    @Override
    public String getResultUseFileCache(String str, double dbl, int delta) {
        return str + dbl + delta;
    }
}
