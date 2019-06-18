package ru.sberbank.school.task13;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultInvocationHandler<T> implements InvocationHandler {

    private final T delegate;
    private final Map<String, Map<String, Object>> cacheMap = new HashMap<>();
    private CacheType cacheType;
    private final Serializer serializer;

    DefaultInvocationHandler(T delegate, CacheType cacheType, Serializer serializer) {
        this.delegate = delegate;
        this.cacheType = cacheType;
        this.serializer = serializer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String prefix = method.getName();

        StringBuilder keyArgs = new StringBuilder();
        if (method.isAnnotationPresent(CachingPolicy.class)) {
            final CachingPolicy annotation = method.getAnnotation(CachingPolicy.class);
            cacheType = annotation.cacheType();
            prefix = annotation.fileNamePrefix();

            int i = 0;
            for (Class clazz : annotation.identityBy()) {
                while (i < args.length) {
                    if (args[i].getClass() == clazz) {
                        keyArgs.append(args[i].hashCode());
                        i++;
                        break;
                    }
                    i++;
                }
            }
        } else {
            for (Object arg : args) {
                keyArgs.append(arg.hashCode());
            }
        }

        String key = keyArgs.toString();
        System.out.println(key);

        Map<String, Object> cache = getCacheForMethod(prefix) ;
        if (cache != null) {
            if (cache.containsKey(key)) {
                return cache.get(key);
            } else {
                Object result = method.invoke(delegate, args);
                cache.put(key, result);
                saveCacheForMethod(prefix, cache);
                return result;
            }
        } else {
            Object result = method.invoke(delegate, args);
            cache = new HashMap<>();
            cache.put(key, result);
            saveCacheForMethod(prefix, cache);
            return result;
        }

    }

    private Map<String, Object> getCacheForMethod(String fileName) throws IOException {
        if (cacheType == CacheType.MEMORY) {
            return cacheMap.getOrDefault(fileName, null);
        } else {
            return serializer.loadCacheFromFile(fileName);
        }
    }

    private void saveCacheForMethod(String fileName, Map<String, Object> cache) throws IOException {
        if (cacheType == CacheType.MEMORY) {
            cacheMap.put(fileName, cache);
        } else {
            serializer.saveCacheToFile(fileName, cache);
        }
    }

}
