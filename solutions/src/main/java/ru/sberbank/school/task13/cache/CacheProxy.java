package ru.sberbank.school.task13.cache;

import lombok.NonNull;
import ru.sberbank.school.task13.cache.annotation.Cache;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CacheProxy {

    public Object cache(@NonNull Object service) {
        return Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(), new Handler(service));
    }

    private class Handler implements InvocationHandler {
        private Object service;
        private Map<String, Object> ramCache;

        Handler(Object service) {
            this.service = service;
            ramCache = new HashMap<>();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Cache annotation = method.getAnnotation(Cache.class);
            if (annotation == null) {
                return method.invoke(service, args);
            }

            if (method.getParameterCount() == 0) {
                throw new RuntimeException("Невозможно кешировать метод, который не принимает входных параметров");
            }
            if (method.getReturnType().equals(void.class)) {
                throw new RuntimeException("Невозможно кешировать метод, который ничего не возвращает");
            }

            switch (annotation.cacheType()) {
                case RAM:
                    return ramStorage(method, args, annotation);

                case DISK:
                    return hddStorage(method, args, annotation);

                default:
                    throw new RuntimeException("Неизвестный метод кеширования");
            }
        }

        private Object hddStorage(Method method, Object[] args, Cache annotation) throws InvocationTargetException,
                                                                            IllegalAccessException {
            File folder = new File(annotation.path());
            if (!folder.isDirectory() || !folder.canWrite() || !folder.canRead()) {
                throw new RuntimeException("Ошибка при попытке обращения к папке " + annotation.path());
            }
            String fileName = buildMethodKey(method, args, annotation) + ".cache";
            File cacheFile = new File(folder.toString() + "/" + fileName);
            if (cacheFile.exists() && cacheFile.canRead()) {
                try {
                    return deserializeFromFile(cacheFile);
                } catch (ClassNotFoundException | IOException e) {
                    cacheFile.delete();
                    return method.invoke(service, args);
                }
            } else {
                Object result = method.invoke(service, args);
                serializeToFile(result, cacheFile);
                return result;
            }
        }

        private Object deserializeFromFile(File file) throws IOException, ClassNotFoundException {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                return input.readObject();
            }
        }

        private Object ramStorage(Method method, Object[] args, Cache annotation) throws InvocationTargetException,
                                                                IllegalAccessException {
            String methodKey = buildMethodKey(method, args, annotation);
            if (ramCache.containsKey(methodKey)) {
                return ramCache.get(methodKey);
            }
            Object result = method.invoke(service, args);
            ramCache.put(methodKey, result);
            return result;
        }

        private String buildMethodKey(Method method, Object[] args, Cache annotation) {
            StringBuilder result = new StringBuilder();
            result.append(method.toString()).append("~");
            Class[] classes = annotation.params();
            for (Object arg : args) {
                if (isMatch(arg, classes)) {
                    result.append(arg.toString()).append("~");
                }
            }
            if (!annotation.name().equals("")) {
                return annotation.name() + "_" + toMd5(result.toString());
            }
            return toMd5(result.toString());
        }

        private boolean isMatch(Object arg, Class[] classes) {
            for (Class cls : classes) {
                if (arg.getClass().equals(cls) || cls.equals(Object.class)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isSerializable(Object object) {
            Class[] interfaces = object.getClass().getInterfaces();
            for (Class iface : interfaces) {
                if (iface.equals(Serializable.class)) {
                    return true;
                }
            }
            return false;
        }

        private void serializeToFile(Object result, File file) {
            if (!isSerializable(result)) {
                throw new RuntimeException("Возвращаемый результат не может быть сериализован");
            }

            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
                output.writeObject(result);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при попытке записи в файл " + file.toString());
            }
        }

        private String toMd5(String st) {
            MessageDigest messageDigest = null;
            byte[] digest = new byte[0];

            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(st.getBytes());
                digest = messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Алгоритм MD5 не найден");
            }

            BigInteger bigInt = new BigInteger(1, digest);
            StringBuilder md5Hex = new StringBuilder(bigInt.toString(16));

            while ( md5Hex.length() < 32 ) {
                md5Hex.append("0").append(md5Hex.toString());
            }

            return md5Hex.toString();
        }
    }
}
