package ru.sberbank.school.task13.cacheproxy;

import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.*;
import java.util.*;

/*
Реализовать класс, порождающий кеширующий прокси — **CacheProxy** с методом **cache(Object service)**,
который принимает ссылку на сервис и возвращает кешированную версию этого сервиса*.
CacheProxy должен тоже принимать в конструкторе некоторые настройки,
например, рутовую папку в которой хранить файлы, дефолтные настройки кеша и тд.
Логика по кешированию должна навешиваться с помощью **DynamicProxy**.
*/
public class CacheProxy<T> {
    private String pathRoot;

    public CacheProxy(@NonNull String pathRoot) {
        if (!new File(pathRoot).isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }
        this.pathRoot = pathRoot;
    }

    public T cache(@NonNull T service) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?>[] classes = service.getClass().getInterfaces();
        DynamicInvocationHandler dynamicInvocationHandler = new DynamicInvocationHandler(service, pathRoot);
        return (T) Proxy.newProxyInstance(classLoader, classes, dynamicInvocationHandler);
    }

    private static class DynamicInvocationHandler implements InvocationHandler {
        private final Object service;
        private Map<String, Object> cachedResults = new HashMap<>();
        private String pathRoot;

        private DynamicInvocationHandler(Object service, String pathRoot) {
            this.service = service;
            this.pathRoot = pathRoot;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            Object ourMethodResult = null;
            System.out.println("Invoked method: " + method.getName());
            if (!method.isAnnotationPresent(Cache.class)) {
                ourMethodResult = method.invoke(service, args);
            } else {
                Cache annotationParams = method.getDeclaredAnnotation(Cache.class);
                if (annotationParams.cacheType() == Cache.cacheType.FILE) {
                    ourMethodResult = toFile(method, args, annotationParams);
                } else {
                    ourMethodResult = toJVMMemory(method, args, annotationParams);
                }
            }

            return ourMethodResult;
        }

        private Object toJVMMemory(Method method, Object[] args, Cache annotationParams) {
            Object ourMethod = null;
            String key = getKey(annotationParams, method, args);
            if (cachedResults.containsKey(key)) {
                ourMethod = cachedResults.get(key);
            } else {
                try {
                    ourMethod = method.invoke(service, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.out.println("Ooops...Can't apply this method, something went wrong");
                }
                cachedResults.put(key, ourMethod);
            }

            return ourMethod;
        }

        private Object toFile(Method method, Object[] args, Cache annotationParams) {
            Object ourMethod = null;
            CacheSerializatorManager cacheSerializatorManager = new CacheSerializatorManager();
            File file = new File(getFileName(annotationParams, method, args));
            if (file.exists()) {
                if (annotationParams.zip()) {
                    try {
                        ourMethod = cacheSerializatorManager.loadMethodFromZip(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("Deserialization mistake, can't fid file with such name");
                    }
                } else {
                    try {
                        ourMethod = cacheSerializatorManager.loadMethodFromFile(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("Deserialization mistake, can't fid file with such name");
                    }
                }
            } else {
                try {
                    ourMethod = method.invoke(service, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (annotationParams.zip()) {
                    try {
                        cacheSerializatorManager.writeMethodToZip(file, ourMethod);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        cacheSerializatorManager.writeMethodToFile(file, ourMethod);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ourMethod;

        }

        private String getFileName(Cache annotationParams, Method method, Object[] args) {
            String fileName;
            String key = getKey(annotationParams, method, args);
            if (annotationParams.zip()) {
                fileName = pathRoot + File.separator + key + ".zip";
            } else {
                fileName = pathRoot + File.separator + key + ".txt";
            }
            return fileName;

        }

        private String getKey(Cache annotationParams, Method method, Object[] args) {
            Class[] classesUniqFields = annotationParams.uniqueFields();
            int id = 0;
            List<Object> rightFields = new ArrayList<>();
            if (classesUniqFields.length > 0) {
                for (Class classUniqFields : classesUniqFields) {
                    for (Object a : args) {
                        if (a.getClass().equals(classUniqFields)) {
                            rightFields.add(a);
                        }
                    }
                }

            } else {
                if (args != null) {
                    rightFields = Arrays.asList(args);
                }
            }
            id = rightFields.hashCode();
            String key = annotationParams.fileNamePrefix();
            if (annotationParams.fileNamePrefix().equals("")) {
                key = method.getName();
            }
            return (id == 0 ? key : key + "_" + id);
        }
    }


}
