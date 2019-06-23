package ru.sberbank.school.task13;

import lombok.RequiredArgsConstructor;
import ru.sberbank.school.util.Solution;

import java.lang.reflect.Proxy;

@Solution(13)
@RequiredArgsConstructor
public class CacheProxy {

    private final String rootPath;

    @SuppressWarnings("unchecked")
    public <T> T cache(T service) {
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new CacheProxyHandler(service, rootPath));
    }
}