package ru.sberbank.school.task13.cacheProxy;

import java.util.ArrayList;
import java.util.List;

public class CacheTestImpl implements CacheTest {
    private int count = 0;
    private String result;

    public CacheTestImpl(String result) {
        this.result = result;
    }

    @Override
    public String cacheInJVM() {
        count++;
        return result;
    }

    @Override
    public String cacheInFile() {
        count++;
        return result;
    }

    @Override
    public void cacheInZip() {
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public List getList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Override
    public int getStringSize(String str, int i) {
        count++;
        return str.length();
    }
}