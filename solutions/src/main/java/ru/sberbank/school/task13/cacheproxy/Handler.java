package ru.sberbank.school.task13.cacheproxy;

import ru.sberbank.school.task13.cacheproxy.annotation.Cache;
import ru.sberbank.school.task13.cacheproxy.annotation.CacheType;
import ru.sberbank.school.task13.cacheproxy.exception.CacheProxyException;
import ru.sberbank.school.task13.cacheproxy.serialization.KryoCacheManager;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Handler implements InvocationHandler {
    private Object service;
    private String rootPath;
    private Map<String, Object> cache = new HashMap<>();
    private KryoCacheManager kryoManager;

    public Handler(Object service, String rootPath) {
        Objects.requireNonNull(service, "Service is not to be null");
        Objects.requireNonNull(rootPath, "Root path is not to be null");
        this.service = service;
        this.rootPath = rootPath;
        kryoManager = new KryoCacheManager(rootPath);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Cache annotation = method.getAnnotation(Cache.class);

        if (annotation == null) {
            return executeMethod(method, args);
        }

        String name = generateName(method, args);

        switch (annotation.cacheType()) {
            case JVM:
                return cacheInJvm(name, method, args);
            case FILE:
                return cacheInFile(name, method, args);
            default:
                throw new IllegalArgumentException("Unsupported cache type");
        }
    }

    private Object cacheInJvm(String key, Method method, Object[] args) {
        Object result = cache.get(key);

        if (result == null) {
            result = executeMethod(method, args);
            cache.put(key, result);
        }

        return result;
    }

    private Object cacheInFile(String name, Method method, Object[] args) {
        String path = rootPath + "/" + name;
        File file = new File(path);

        kryoManager.register(method.getReturnType());

        if (file.exists()) {
            return kryoManager.loadObject(name);
        } else {
            Object result = executeMethod(method, args);
            kryoManager.saveObject(name, result);

            return result;
        }

    }

    private Object executeMethod(Method method, Object[] args) {
        try {
            return method.invoke(service, args);
        } catch (IllegalAccessException e) {
            throw new CacheProxyException("No access to the method", e);
        } catch (InvocationTargetException e) {
            throw new CacheProxyException("Method throws an exception", e);
        }
    }

    private String generateName(Method method, Object[] args) {
        Cache annotation = method.getAnnotation(Cache.class);

        Object[] argsForIdentity = annotation.identityBy().length != 0
                ? choiceArgumentsForIdentity(args, annotation.identityBy())
                : args;

        String name = annotation.fileNamePrefix().isEmpty() ? method.getName() : annotation.fileNamePrefix();

        StringBuilder key = new StringBuilder(name);

        for (Object arg : argsForIdentity) {
            key.append("_").append(arg.hashCode());
        }
        if (annotation.cacheType() == CacheType.FILE) {
            key.append(".txt");
        }

        return key.toString();
    }

    private Object[] choiceArgumentsForIdentity(Object[] args, Class[] identityByClasses) {
        Object[] argsForIdentity = new Object[identityByClasses.length];

        for (int i = 0, j = 0; i < identityByClasses.length; i++) {
            for (; j < args.length; j++) {
                if (identityByClasses[i] == args[i].getClass()) {
                    argsForIdentity[i] = args[i];
                    break;
                }
            }
        }

        for (Object object : argsForIdentity) {
            if (object == null) {
                throw new IllegalArgumentException(
                        "Error specifying arguments to determine the uniqueness of the result. "
                        + "Check that the arguments are in the correct order "
                        + "and that instead of primitives their wrapper is indicated");
            }
        }

        return argsForIdentity;
    }

}
