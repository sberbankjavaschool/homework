package ru.sberbank.school.task13.cacheproxy;

import java.lang.reflect.Proxy;

public class CacheProxy {
    private final String  filePath;

    public CacheProxy(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unchecked")
    public <T> T cache(T object) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(),
                new CachingInvocationHandler<>(object, filePath));
    }
}
