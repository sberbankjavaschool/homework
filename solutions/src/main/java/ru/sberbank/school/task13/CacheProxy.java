package ru.sberbank.school.task13;

import ru.sberbank.school.task13.util.Handler;

import java.io.*;
import java.lang.reflect.Proxy;
import java.util.*;

public class CacheProxy {

    private final File directory;

    public CacheProxy(String directoryName) {

        Objects.requireNonNull(directoryName,
                "При кешировании в файл переданный путь к директории не должен быть null");
        directory = new File(directoryName);

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Переданный путь к директории кеширования не указывает "
                    + "на директорию");
        }

    }

    @SuppressWarnings("unchecked")
    public <T> T cache(Object service) {
        return (T) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new Handler(service, directory));
    }

}
