package ru.sberbank.school.task13;

import java.lang.reflect.Proxy;

public class CacheProxyImpl {
    private final CacheType cacheType;
    private final String defaultFilePath;

    public CacheProxyImpl(String defaultFilePath) {
        this.cacheType = CacheType.MEMORY;
        this.defaultFilePath = defaultFilePath;
    }

    public CacheProxyImpl(String defaultFilePath, CacheType cacheType) {
        this.cacheType = cacheType;
        this.defaultFilePath = defaultFilePath;
    }

    @SuppressWarnings("unchecked")
    public <T> T cache(T object) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new DefaultInvocationHandler<>(object, cacheType, new Serializer(defaultFilePath)));
    }
}
