package ru.sberbank.school.task13.cacheproxy;

import java.lang.reflect.Proxy;

public class CacheProxy {
    private String rootPath;

    public CacheProxy(String rootPath) {
        this.rootPath = rootPath;
    }

    public Object cache(Object service) {
        return Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new Handler(service, rootPath));
    }
}
