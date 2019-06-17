package ru.sberbank.school.task13.cacheproxy.cache;

import lombok.NonNull;

import java.lang.reflect.Proxy;

import static java.lang.ClassLoader.getSystemClassLoader;

public class CacheProxy<T> {
    private final String fileDirectory;

    public CacheProxy(@NonNull String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public  <T> T cache(T delegate) {
        return (T) Proxy.newProxyInstance(getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CacheProxyImpl(delegate, fileDirectory));
    }
}
