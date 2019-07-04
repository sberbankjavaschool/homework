package ru.sberbank.school.task13.cacheproxy;

import lombok.NonNull;
import ru.sberbank.school.task13.cacheproxy.annotations.Cache;
import ru.sberbank.school.task13.cacheproxy.annotations.CacheType;
import ru.sberbank.school.task13.cacheproxy.exceptions.CachingHandlerException;
import ru.sberbank.school.task13.cacheproxy.serializers.CachingSerializer;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CachingInvocationHandler<T> implements InvocationHandler {
    private final T object;
    private final Map<String, Object> cached;
    private final CachingSerializer cachingSerializer;
    private final String path;

    public CachingInvocationHandler(@NonNull T object, @NonNull String path) {
        this.object = object;
        this.cached = new HashMap<>();
        this.path = path;
        this.cachingSerializer = new CachingSerializer(path);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            Cache annotation = method.getAnnotation(Cache.class);
            String key = createKey(annotation, method, args);

            if (annotation.cacheType() == CacheType.RAM) {
                return cacheToRam(method, args, key);
            } else {
                return cacheToFile(method, args, key);
            }
        } else {
            System.out.println("i'm here");
            return invokeMethod(method, args);
        }

    }

    private Object cacheToRam(Method method, Object[] args, String key) {
        if (cached.containsKey(key)) {
            return cached.get(key);
        } else {
            Object result = invokeMethod(method, args);
            cached.put(key, result);

            return result;
        }
    }

    private Object cacheToFile(Method method, Object[] args, String key) {
        File cacheFile = new File(path + File.separator + key + ".cache");
        cachingSerializer.initialize(method.getReturnType());

        if (cacheFile.exists()) {
            return cachingSerializer.readFromFile(key);
        } else {
            Object result = invokeMethod(method, args);
            cachingSerializer.writeToFile(key, result);

            return result;
        }
    }


    private Object invokeMethod(Method method, Object[] args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new CachingHandlerException("Не получается получить доступ к методу", e);
        } catch (InvocationTargetException e) {
            throw new CachingHandlerException("Метод выбросил исключение", e);
        }
    }

    private String createKey(Cache annotation, Method method, Object[] args) {
        String name = method.getName();

        if (annotation.fileNamePrefix().length() != 0) {
            name = annotation.fileNamePrefix();
        }

        StringBuilder key = new StringBuilder().append(name);

        int i  = 0;
        Class[] identityBy = annotation.identityBy().length != 0 ? annotation.identityBy() : method.getParameterTypes();

        for (Class clazz : identityBy) {
            int j = i;
            for (; j < args.length; j++) {
                if (args[j].getClass() == clazz) {
                    key.append(args[j].hashCode());
                    i++;
                    break;
                }
            }
        }

        return key.toString();
    }
}
