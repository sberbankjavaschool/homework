package ru.sberbank.school.task13.cacheProxy.cache;

import java.lang.reflect.Proxy;

public class CacheProxy {

    private final String filePath;


    public CacheProxy(String filePath) {
        this.filePath = filePath;
    }

    public Object cache (Object service){
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                service.getClass().getInterfaces(),
                new HandlerProxy(service, filePath));
    }
}
