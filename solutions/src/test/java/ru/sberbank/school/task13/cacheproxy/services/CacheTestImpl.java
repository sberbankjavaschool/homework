package ru.sberbank.school.task13.cacheproxy.services;

import java.util.ArrayList;
import java.util.List;

public class CacheTestImpl implements CacheTest {
    private int callCounter = 0;
    private String result;

    public CacheTestImpl(String result) {
        this.result = result;
    }

    @Override
    public String getResultUseMemoryCache() {
        callCounter++;
        return result;
    }

    @Override
    public String getResultUseFileCache() {
        callCounter++;
        return result;
    }

    @Override
    public void zipFileTest() {
    }

    @Override
    public int getCallCounter() {
        return callCounter;
    }

    @Override
    public List createList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Override
    public int stringSize(String str, int useless) {
        callCounter++;
        return str.length();
    }
}
