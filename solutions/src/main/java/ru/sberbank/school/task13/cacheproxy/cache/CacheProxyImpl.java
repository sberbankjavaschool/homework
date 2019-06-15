package ru.sberbank.school.task13.cacheproxy.cache;

import lombok.NonNull;
import ru.sberbank.school.task13.cacheproxy.annotation.Cache;
import ru.sberbank.school.task13.cacheproxy.exception.CacheProxyException;
import ru.sberbank.school.task13.cacheproxy.serialization.SerializationManager;
import ru.sberbank.school.task13.cacheproxy.utils.CacheProxyUtils;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CacheProxyImpl implements InvocationHandler {
    private Object delegate;
    private Map<String, Object> cache;
    private final String fileDirectory;
    private final SerializationManager serializationManager;

    public CacheProxyImpl(@NonNull Object delegate, String fileDirectory) {
        this.delegate = delegate;
        this.cache = new HashMap<>();
        this.fileDirectory = fileDirectory;
        this.serializationManager = new SerializationManager();
    }

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args)
            throws CacheProxyException {
        Object result = null;

        if (!method.isAnnotationPresent(Cache.class)) {
            result = methodInvoke(method, delegate, args);
        } else {
            Cache annotations = method.getDeclaredAnnotation(Cache.class);
            String key = CacheProxyUtils.getKey(annotations.identityBy(),
                    annotations.key(),
                    method.getName(),
                    args);
            switch (annotations.getCacheType()) {
                case MEMORY:
                    result = getMemoryCache(method, args, key, annotations);
                    break;
                case FILE:
                    result = getFileCache(method, args, key, annotations);
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private Object getMemoryCache(Method method, Object[] args, String key, Cache annotations)
            throws CacheProxyException {
        Object result;
        result = cache.get(key);
        if (result == null) {
            result = methodInvoke(method, delegate, args);
            if (result instanceof List) {
                result = CacheProxyUtils.cutList((ArrayList) result, annotations.listSize());
            }
            cache.put(key, result);
        }
        return result;
    }

    private Object getFileCache(Method method, Object[] args, String key, Cache annotations)
            throws CacheProxyException {
        Object result;
        File file = new File(fileDirectory + File.separator + key);
        if (file.exists()) {
            if (annotations.zip()) {
                result = serializationManager.readMethodFromZip(file);
            } else {
                result = serializationManager.readMethodFromFile(file);
            }
        } else {
            result = methodInvoke(method, delegate, args);
            if (result instanceof List) {
                result = CacheProxyUtils.cutList((ArrayList) result, annotations.listSize());
            }
            if (annotations.zip()) {
                serializationManager.writeInZip(file, result);
            } else {
                serializationManager.writeMethodInFile(file, result);
            }
        }
        return result;
    }

    private Object methodInvoke(Method method, Object delegate, Object[] args)
            throws CacheProxyException {
        try {
            return method.invoke(delegate, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }
}