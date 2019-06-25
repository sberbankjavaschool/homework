package ru.sberbank.school.task13.cache;

import lombok.NonNull;
import ru.sberbank.school.task13.cache.annotation.Cache;
import ru.sberbank.school.task13.cache.annotation.CacheType;
import ru.sberbank.school.task13.cache.excaption.CacheException;

import java.io.*;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class CacheProxy {

    private final String filesDirectory;

    public CacheProxy(@NonNull String filesDirectory) {
        if (!(new File(filesDirectory).isDirectory())) {
            throw new IllegalArgumentException("Указанная директория не существует: " + filesDirectory);
        }
        this.filesDirectory = filesDirectory;
    }

    public Object cache(Object service) {
        return Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new Handler(service));
    }

    private class Handler implements InvocationHandler {

        private final Object service;
        private Map<String, Object> cacheMap;

        Handler(@NonNull Object service) {
            this.service = service;
            cacheMap = new HashMap<>();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException,
                IllegalAccessException, IllegalArgumentException {

            Cache annotation = method.getAnnotation(Cache.class);
            if (annotation == null) {
                return method.invoke(service, args);
            }

            if (method.getReturnType().equals(void.class)) {
                throw new CacheException("Вы хотите кешировать метод, который ничего не возвращает: "
                        + method.getName());
            }
            if (method.getParameterTypes().length == 0) {
                throw new CacheException("Вы хотите кешировать метод, у которого нет параметров: "
                        + method.getName());
            }

            CacheType cacheType = annotation.cacheType();
            switch (cacheType) {
                case FILE:
                    try {
                        return invokeFile(method, args);
                    } catch (NotSerializableException e) {
                        System.out.println(e.getMessage());
                        return invokeJvm(method, args);
                    }
                case JVM:
                    return invokeJvm(method, args);
                default:
                    throw new CacheException("Неизвестный тип кеширования!");
            }
        }

        private Object invokeJvm(Method method, Object[] args) throws InvocationTargetException,
                IllegalAccessException {

            String key = getKeyForMethod(method, args);
            if (cacheMap.containsKey(key)) {
                return cacheMap.get(key);
            }
            Object result = null;
            result = method.invoke(service, args);
            cacheMap.put(key, result);

            return result;
        }

        private Object invokeFile(Method method, Object[] args) throws InvocationTargetException,
                IllegalAccessException, NotSerializableException {

            String fileName = getKeyForMethod(method, args) + ".cache";
            File file = new File(filesDirectory + "/" + fileName);
            if (file.exists()) {
                try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                    return input.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Ошибка при чтения результата из файла!");
                }
            } else {
                Object result = null;
                result = method.invoke(service, args);
                cacheToFile(fileName, result);
                return result;
            }
            return null;
        }

        private void cacheToFile(String filename, Object result) throws NotSerializableException {
            if (!isSerializable(result)) {
                throw new NotSerializableException("Ошибка при кешировании в файл: "
                        + result.getClass().getSimpleName() + " должен реализовать Serializable.");
            }
            String fullName = filesDirectory + "/" + filename;

            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fullName))) {
                output.writeObject(result);
            } catch (IOException e) {
                CacheException eCache = new CacheException("Ошибка при работе с файлом.");
                eCache.initCause(e);
                throw eCache;
            }
        }

        private String getKeyForMethod(Method method, Object[] args) {
            Cache annotation = method.getAnnotation(Cache.class);
            StringBuilder key = new StringBuilder();

            String nameMethod = annotation != null && !annotation.key().isEmpty()
                        ? annotation.key() : method.getName();
            key.append(nameMethod);

            Object[] argsForKey = getArgsForKey(method, args);
            if (argsForKey != null) {
                for (Object arg : argsForKey) {
                    key.append(".").append(arg.toString());
                }
            }

            return key.toString();
        }

        private Object[] getArgsForKey(Method method, Object[] args) {
            Cache annotation = method.getAnnotation(Cache.class);
            if (annotation != null) {
                int[] indexForIdentity = annotation.indexIdentity();
                int n = indexForIdentity.length;
                if (n != 0) {
                    Object[] argsForKey = new Object[n];
                    for (int i = 0; i < n; i++) {
                        argsForKey[i] = args[indexForIdentity[i]];
                    }
                    return argsForKey;
                }
            }
            return args;
        }

        private boolean isSerializable(Object result) {
            for (Class<?> anInterface : result.getClass().getInterfaces()) {
                if (anInterface.equals(Serializable.class)) {
                    return true;
                }
            }
            return false;
        }
    }
}

