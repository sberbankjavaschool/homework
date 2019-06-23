package ru.sberbank.school.task13;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.sberbank.school.task13.annotations.Cache;
import ru.sberbank.school.task13.exceptions.CacheProxyHandlerException;
import ru.sberbank.school.task13.exceptions.FileCacheException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.io.File.separator;

@RequiredArgsConstructor
public class CacheProxyHandler<T> implements InvocationHandler {

    private final T service;
    private final String rootPath;
    private final Map<String, Object> memoryCache = new HashMap<>();
    private final KryoCacheService kryoService = new KryoCacheService();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Cache annotation = method.getAnnotation(Cache.class);
        if (Objects.isNull(annotation)) {
            return execute(method, args);
        }
        String name = getName(method, args);

        switch (annotation.cacheType()) {
            case JVM:
                return jvmCache(name, method, args);
            case FILE:
                return fileCache(name, method, args);
            default:
                throw new IllegalArgumentException("Missing caching type " + annotation.cacheType());
        }
    }

    private Object execute(Method method, Object[] args) {
        try {
            return method.invoke(service, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new CacheProxyHandlerException("Can't invoke method: " + method.getName(), e);
        }
    }

    private String getName(Method method, Object[] args) {
        Cache annotation = method.getAnnotation(Cache.class);

        StringBuilder builder = new StringBuilder(annotation.fileNamePrefix());
        builder.append(service.hashCode());
        builder.append("_");
        builder.append(method.getName());

        if (annotation.identityBy().length > 0) {
            args = annotation.identityBy();
        }
        if (!Objects.isNull(args)) {
            for (Object arg : args) {
                builder.append("_");
                builder.append(arg.getClass().getName());
                builder.append(arg.hashCode());
            }
        }
        return builder.toString();
    }

    private Object jvmCache(String name, Method method, Object[] args) {
        Object result = memoryCache.get(name);
        if (Objects.isNull(result)) {
            result = execute(method, args);
            memoryCache.put(name, result);
        }
        return result;
    }

    private Object fileCache(String name, Method method, Object[] args) {
        File file = new File(rootPath + separator + name);
        Object result;
        if (file.exists()) {
            result = kryoService.fetch(file);
        } else {
            result = execute(method, args);
            kryoService.cache(file, result);
        }
        return result;
    }

    private class KryoCacheService {
        private final Kryo kryo = new Kryo();

        {
            kryo.setReferences(true);
        }

        private void cache(@NonNull File file, @NonNull Object object) throws FileCacheException {
            try (FileOutputStream outputStream = new FileOutputStream(file);
                 Output output = new Output(outputStream)) {
                kryo.register(object.getClass());
                kryo.writeClassAndObject(output, object);
            } catch (IOException | NullPointerException | KryoException e) {
                throw new FileCacheException("Can't cache object: " + file.getName(), e);
            }
        }

        private Object fetch(@NonNull File file) throws FileCacheException {
            try (FileInputStream inputStream = new FileInputStream(file);
                 Input input = new Input(inputStream)) {
                return kryo.readClassAndObject(input);
            } catch (IOException | NullPointerException | ClassCastException | KryoException e) {
                throw new FileCacheException("Can't fetch cached object: " + file.getName(), e);
            }
        }
    }
}