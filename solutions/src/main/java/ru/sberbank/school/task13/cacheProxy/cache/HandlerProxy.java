package ru.sberbank.school.task13.cacheProxy.cache;

import lombok.NonNull;
import ru.sberbank.school.task13.cacheProxy.annotation.Cache;
import ru.sberbank.school.task13.cacheProxy.exception.CacheProxyException;
import ru.sberbank.school.task13.cacheProxy.kryo.SerializableManager;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerProxy implements InvocationHandler {

    private Object service;
    private Map<String, Object> cache;
    private final String filePath;
    private final SerializableManager serializableManager;

    public HandlerProxy(@NonNull Object service, String filePath) {
        this.service = service;
        this.cache = new HashMap<>();
        this.filePath = filePath;
        this.serializableManager = new SerializableManager();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;

        if (!method.isAnnotationPresent(Cache.class)) {
            result = method.invoke(service, args);
        } else {
            Cache annotation = method.getDeclaredAnnotation(Cache.class);
            String key = getKey(annotation, method, args);
            switch (annotation.getCacheType()) {
                case JVM:
                    result = cacheInMemory(key, method, args, annotation);
                    break;
                case FILE:
                    result = cacheInFile(key, method, args, annotation);
                    break;
            }
        }
        return result;
    }

    private Object cacheInMemory(String key,
                                 Method method,
                                 Object[] args,
                                 Cache annotation) throws CacheProxyException {
        Object result;
        result = cache.get(key);
        if (result == null) {
            result = checkOfReturnTypeOnList(method, args, annotation);
            cache.put(key, result);
            System.out.println();
        }

        return result;
    }

    private Object cacheInFile(String key,
                               Method method,
                               Object[] args,
                               Cache annotation) throws CacheProxyException {
        Object result;
        File file = new File(filePath + File.separator + key);

        if (file.exists()) {
            if (annotation.zip()) {
                result = serializableManager.readFromZip(file);
            } else {
                result = serializableManager.readFromFile(file);
            }
        } else {
            result = checkOfReturnTypeOnList(method, args, annotation);
            if (annotation.zip()) {
                serializableManager.writeInZip(file, result);
            } else {
                serializableManager.writeInFile(file, result);
            }
        }

        return result;
    }

    private Object checkOfReturnTypeOnList(Method method, Object[] args, Cache annotation) throws CacheProxyException {
        Object result;
        try {
            result = method.invoke(service, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CacheProxyException(e.getMessage(), e);
        }
        if (result instanceof List) {
            result = getSubList((List<?>) result, annotation.listSize());
        }
        return result;
    }

    public static String getKey(Cache annotation, Method method, Object[] args) {
        List<Object> list = new ArrayList<>();
        if (annotation.identityBy().length > 0) {
            for (Object obj : annotation.identityBy()) {
                for (Object arg : args) {
                    if (arg.getClass() == obj) {
                        list.add(arg);
                    }
                }
            }
        }
        return (annotation.key().equals("") ? method.getName() : annotation.key()) + list.hashCode();

    }

    private <T> List<T> getSubList(List<T> list, int cutSize) throws CacheProxyException {
        if (cutSize == -1) {
            return list;
        } else {
            if (cutSize < 0 || cutSize > list.size())
                throw new CacheProxyException("Wrong range!", new ArrayIndexOutOfBoundsException());
            return new ArrayList<>(list.subList(0, cutSize));
        }
    }
}
