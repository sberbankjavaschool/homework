package ru.sberbank.school.task13.cacheproxy;

import lombok.NonNull;
import ru.sberbank.school.task13.cacheproxy.exception.CachedException;
import ru.sberbank.school.task13.cacheproxy.util.Cache;
import ru.sberbank.school.task13.cacheproxy.util.CacheType;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CacheProxy {

    private final String directory;

    public CacheProxy(@NonNull String rootDirectoryName) {

        if (!(new File(rootDirectoryName).isDirectory())) {
            throw new IllegalArgumentException("указанная директория не существует");
        }
        this.directory = rootDirectoryName;
    }

    public Object cache(@NonNull Object service) {
        Handler handler = new Handler(service);

        return Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                handler);
    }

    public String getDirectory() {
        return directory;
    }

    private class Handler implements InvocationHandler {
        private final Object service;
        private final Map<String, Object> methodMap;

        Handler(Object service) {
            this.service = service;
            this.methodMap = new HashMap<>();
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws IllegalAccessException, IllegalArgumentException,
                InvocationTargetException {
            if (!method.getReturnType().equals(void.class) && method.isAnnotationPresent(Cache.class)) {
                String methodWithArgsName = getMethodWithArgsName(method, args);
                try {
                    if (isCached(method, methodWithArgsName)) {
                        return getCachedResult(method, args);
                    } else {
                        return cacheMethod(method, args);
                    }
                } catch (CachedException e) {
                    System.err.println(e.getMessage());
                    return method.invoke(service, args);
                }
            } else {
                return method.invoke(service, args);
            }
        }

        private Object getCachedResult(Method method, Object[] args) {
            String methodWithArgsName = getMethodWithArgsName(method, args);
            if (method.getAnnotation(Cache.class).cacheType() == CacheType.FILE) {
                File file = new File(directory + File.separator + methodWithArgsName);
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    return in.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    throw new CachedException("Возникла ошибка при загрузке результата из файла");
                }
            } else {
                return methodMap.get(methodWithArgsName);
            }
        }

        private Object cacheMethod(Method method, Object[] args)
                throws InvocationTargetException, IllegalAccessException {
            Cache cacheAnnotation = method.getAnnotation(Cache.class);
            if (cacheAnnotation.cacheType() == CacheType.JVM) {
                Object result = method.invoke(service, args);
                methodMap.put(getMethodWithArgsName(method, args), result);
                return result;
            } else {
                Object result = method.invoke(service, args);
                try {
                    writeFile(method, args, result);
                } catch (NotSerializableException | CachedException e) {
                    throw new CachedException(e.getMessage());
                }
                return result;
            }
        }

        private void writeFile(Method method, Object[] args, Object result) throws NotSerializableException {
            if (!isSerializable(result)) {
                throw new NotSerializableException("результат метода нельзя сериализовать");
            } else {
                File file = new File(directory + File.separator + getMethodWithArgsName(method, args));
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                    out.writeObject(result);
                } catch (IOException e) {
                    throw new CachedException("Возникла ошибка при попытке сохранить результат в файл");
                }
            }
        }

        private boolean isSerializable(Object result) {
            for (Class<?> anInterface : result.getClass().getInterfaces()) {
                if (anInterface.equals(Serializable.class)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isCached(Method method, String methodWithArgsName) {
            Cache cacheAnnotation = method.getAnnotation(Cache.class);
            if (cacheAnnotation.cacheType() == CacheType.FILE) {
                return checkFile(methodWithArgsName);
            } else {
                return methodMap.keySet().contains(methodWithArgsName);
            }
        }

        private boolean checkFile(String methodWithArgsName) {
            return (new File(directory + File.separator + methodWithArgsName)).exists();
        }

        private String getMethodWithArgsName(Method method, Object[] args) {
            StringBuilder name = new StringBuilder(method.getName());
            name.append(method.getParameterCount());
            Cache cacheAnnotation = method.getAnnotation(Cache.class);
            if (method.getParameterCount() != 0) {
                if (cacheAnnotation.identityBy().length == 0) {
                    for (Object arg : args) {
                        name.append(arg.getClass());
                        name.append(arg.hashCode());
                    }
                } else {
                    for (Object arg : cacheAnnotation.identityBy()) {
                        name.append(arg.getClass());
                        name.append(arg.hashCode());
                    }
                }
            }
            return name.toString();
        }
    }
}