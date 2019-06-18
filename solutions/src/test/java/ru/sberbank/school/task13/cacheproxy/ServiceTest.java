package ru.sberbank.school.task13.cacheproxy;

import java.util.concurrent.locks.Lock;

public class ServiceTest implements ServiceTestInterface {
    @Override
    public String cacheInJvm(String arg) {
        System.out.println("Метод выполнен и записан в кеш");
        return "test1";
    }

    @Override
    public Integer cacheInFile(int arg1, String arg2) {
        System.out.println("Метод выполнен и записан в кеш");
        return arg1;
    }

    @Override
    public Integer sumWithPropertyIdentityByFirstArgument(int arg1, int arg2) {
        System.out.println("Метод выполнен и записан в кеш");
        return arg1 + arg2;
    }

    @Override
    public String sumStringWithPropertyIdentityByFirstArgument(int arg1, String arg2, String arg3) {
        return arg2 + arg3;
    }
}
