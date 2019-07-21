package ru.sberbank.school.task13.cache;

import ru.sberbank.school.task13.cache.exceptions.CacheProxyException;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Mart
 * 10.07.2019
 **/
public class CacheProxy {
    private String directoryPath;

    public CacheProxy(String directoryPath) {
        String message = "path to directory can not be ";
        Objects.requireNonNull(directoryPath,message + "null");
        if (directoryPath.isEmpty()) {
            throw new IllegalArgumentException(message + "empty");
        }

        File directory = new File(directoryPath);
        this.directoryPath = getCheckedPath(directory, directoryPath);
    }

    private static String getCheckedPath(File directory, String directoryPath) {
        String path;
        if (directory.exists() && directory.isDirectory()) {
            path = directoryPath;
        } else {
            boolean created = directory.mkdir();
            if (created) {
                path = directoryPath;
            } else {
                throw new IllegalArgumentException("error! directory can not be created");
            }
        }
        return path;
    }

    public Object cache(Object service) {
        Class c1ass = service.getClass();
        return Proxy.newProxyInstance(c1ass.getClassLoader(),
                c1ass.getInterfaces(),
                new CacheProxyHandler(service));
    }

    private class CacheProxyHandler implements InvocationHandler {
        private Object service;
        private Map<String, Object> cacheMap;

        CacheProxyHandler(Object service) {
            this.service = service;
            this.cacheMap = new HashMap<>();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws InvocationTargetException, IllegalAccessException {

            Object result = null;
            Cache cacheAnnotation = method.getAnnotation(Cache.class);

            if (!handlerInvokeMethodArgsCheck(cacheAnnotation, method, args)) {
                return method.invoke(service, args);
            }

            CacheType cacheType = cacheAnnotation.cacheType();

            if (cacheType.equals(CacheType.JVM)) {
                result = invokeJVM(method, args);
            } else if (cacheType.equals(CacheType.FILE)) {
                result = invokeFile(method, args);
            } else {
                throw new CacheProxyException("unknown Cache type is declared in " + method.getName());
            }

            return result;
        }

        private boolean handlerInvokeMethodArgsCheck(Annotation cacheAnnotation, Method method, Object[] args) {
            if (cacheAnnotation == null) {
                return false;
            }

            if (method.getReturnType().equals(void.class)) {
                throw new IllegalArgumentException("called method can not be void");
            }

            return true;
        }

        private Object invokeJVM(Method method, Object[] args) throws InvocationTargetException,
                IllegalAccessException {

            String keyFromInput = getKeyToCache(method, args);

            Object result = findInCache(keyFromInput);

            if (result == null) {
                result = method.invoke(service, args);
                cacheMap.put(keyFromInput, result);
            }

            return result;
        }

        private Object invokeFile(Method method, Object[] args)
                throws InvocationTargetException, IllegalAccessException {

            String key = getKeyToCache(method, args);
            String fileName = key + ".cache";

            File file = new File(directoryPath + File.separator + fileName);
            Object result = null;
            File fileInCache = findInDirectory(file);

            if (fileInCache != null) {
                result = loadFromCacheFile(fileInCache);
            } else {
                result = method.invoke(service, args);
                writeToCacheFile(file, result);
            }

            return result;
        }

        private Object findInCache(String toFind) {
            Object result = null;

            if (cacheMap.containsKey(toFind)) {
                result = cacheMap.get(toFind);
            } else {
                for (String keyFromCacheMap : cacheMap.keySet()) {
                    if (keyFromCacheMap.contains(toFind)) {
                        result = cacheMap.get(keyFromCacheMap);
                    }
                }
            }

            return result;
        }

        private File findInDirectory(File toFind) {
            String fullNameToFind = isZip(toFind.getAbsolutePath())
                    ? (toFind.getAbsolutePath() + ".zip") : toFind.getAbsolutePath();

            File result = null;
            File toCheck = new File(fullNameToFind);

            if (toCheck.exists() && ! toCheck.isDirectory()) {
                result = new File(fullNameToFind);
            }
            return result;
        }

        private Object loadFromCacheFile(File fileToLoad) {
            Object result = null;

            if (isZip(fileToLoad.getName())) {
                result = readZipFile(fileToLoad);
            } else {
                result = readFromFile(fileToLoad);
            }

            return result;
        }

        private Object readFromFile(File file) {
            Object result = null;

            try (FileInputStream fin = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fin)) {

                result = oin.readObject();

            } catch (ClassNotFoundException | IOException e) {
                throw new CacheProxyException("Error occurred while loading file: " + file.getName(), e);
            }
            return result;
        }

        private Object readZipFile(File file) {
            Object result = null;
            try {
                File zipFileToGet = new File(file.getAbsolutePath());

                if (zipFileToGet.exists()) {
                    try (ZipFile zipFile = new ZipFile(file.getAbsolutePath())) {
                            ZipEntry zipEntry = zipFile.getEntry(
                                file.getName().replace(".zip", ""));

                        try (InputStream in = zipFile.getInputStream(zipEntry)) {
                            byte[] bytes = getBytes(in);

                            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                                result = ois.readObject();
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new CacheProxyException("Error occurred while loading file: "
                        + file.getName() + "from a zip", e);
            }

            return result;
        }

        private byte[] getBytes(InputStream in) throws IOException {
            byte[] buffer = new byte[in.available()];
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                int r;
                while ((r = in.read(buffer)) != -1) {
                    baos.write(buffer,0, r);
                }

                return baos.toByteArray();
            }
        }

        private void writeToCacheFile(File file, Object toSave) {
            try {
                isSerializableCheck(toSave);

                if (isZip(file.getName())) {
                    writeZipFile(file, toSave);
                } else {
                    writeToFile(file, toSave);
                }
            } catch (IOException e) {
                throw new CacheProxyException("Error occurred while writing object: "
                        + toSave + " to file: " + file.getName(), e);
            }
        }

        private void writeZipFile(File file, Object toSave) throws IOException {
            isSerializableCheck(toSave);

            File zipFile = new File(file.getAbsolutePath() + ".zip");
            try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zout.putNextEntry(zipEntry);
                oos.writeObject(toSave);
                zout.write(baos.toByteArray());

                zout.closeEntry();
            }
        }

        private void writeToFile(File file, Object toSave) throws IOException {
            try (FileOutputStream fout = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fout)) {

                out.writeObject(toSave);
            }
        }

        private String getKeyToCache(Method method, Object[] args) {
            Cache annotation = method.getAnnotation(Cache.class);
            StringBuilder key = new StringBuilder();
            boolean isZip = annotation.zip();
            String methodName = !annotation.serviceName().isEmpty()
                    ? annotation.serviceName() : method.getName();

            if (isZip) {
                key.append("zip");
            }

            key.append(methodName);
            String identityArgs = getStringFromIdentityArgs(method, args);
            key.append(identityArgs);

            return key.toString();
        }

        private String getStringFromIdentityArgs(Method method, Object[] args) {
            Cache annotation = method.getAnnotation(Cache.class);
            StringBuilder identityArgs = new StringBuilder();
            Class[] classes = annotation.identityBy();
            List<Class> argsClassesList = new ArrayList<>();
            if (args != null) {
                for (Object argument : args) {
                    argsClassesList.add(argument.getClass());
                }
            }

            String result = "";

            if (classes.length != 0) {
                for (Class identity : classes) {
                    if (argsClassesList.contains(identity)) {
                        identityArgs.append(identity.getSimpleName());
                    }
                }

                if (identityArgs.length() == 0) {
                    identityArgs.append(method.getReturnType().getSimpleName());
                }
                result = identityArgs.toString();
            } else {
                result = method.getReturnType().getSimpleName();
            }

            return result;
        }

        private boolean isZip(String fileName) {
            return fileName.contains("zip");
        }

        private void isSerializableCheck(Object object) {
            boolean isSerializable = false;
            for (Class<?> lnterface : object.getClass().getInterfaces()) {
                if (lnterface.equals(Serializable.class)) {
                    isSerializable = true;
                }
            }

            if (!isSerializable) {
                throw new CacheProxyException("Object to save should be serializable");
            }
        }
    }
}
