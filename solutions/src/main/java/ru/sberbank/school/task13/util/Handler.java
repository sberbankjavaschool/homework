package ru.sberbank.school.task13.util;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Handler implements InvocationHandler {
    private final Object service;
    private final File directory;
    private final Map<String, Object> memoryCache = new HashMap<>();

    public Handler(Object service, File directory) {
        Objects.requireNonNull(directory, "Переданный путь к директории не должен быть null");
        Objects.requireNonNull(service, "Переданный сервис не должен быть null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Переданный путь к директории кеширования не указывает "
                    + "на директорию");
        }
        this.directory = directory;
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException, IOException {

        if (method.getReturnType() != void.class && method.isAnnotationPresent(Cache.class)) {
            Object result;
            Cache annotation = method.getAnnotation(Cache.class);
            boolean zip = annotation.zip();
            String prefixName = annotation.fileNameOrKey().isEmpty() ? method.getName()
                    : annotation.fileNameOrKey();

            Object[] identityByArgs = getIdentityByArgs(args, annotation.identityBy(), method);
            String fileName = getFullName(prefixName, identityByArgs);

            if (annotation.cacheType() == CacheType.FILE) {

                String filePath = directory.getPath() + File.separator + fileName;

                result = tryToLoadFromFile(filePath, fileName, zip);

                if (result == null) {
                    result = method.invoke(service, args);

                    saveToFile(filePath, fileName, zip, result);

                    return result;
                }

            } else {
                result = memoryCache.get(fileName);

                if (result == null) {
                    result = method.invoke(service, args);
                    memoryCache.put(fileName, result);
                }
            }

            return result;
        }

        return method.invoke(service, args);
    }

    private void saveToFile(String fullFileName, String fileName, boolean zip, Object result)
            throws NotSerializableException {
        if (!isSerializable(result)) {
            throw new NotSerializableException("Произошла ошибка при попытке кэширования результата в файл. "
                    + "Класс " + result.getClass().getSimpleName() + " должен реализовать интерфейс Serializable");
        }

        if (zip) {
            File zipFile = new File(fullFileName + ".zip");

            try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                zout.putNextEntry(new ZipEntry(fileName));
                oos.writeObject(result);
                zout.write(baos.toByteArray());
                zout.closeEntry();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            File file = new File(fullFileName);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(result);
            } catch (IOException e) {
                System.out.println("Возникла ошибка при попытке сохранить результат в файл");
                e.printStackTrace();
            }
        }
    }

    private boolean isSerializable(Object result) {
        return Arrays.stream(result.getClass().getInterfaces())
                .anyMatch(i -> i.getSimpleName().equals("Serializable"));
    }

    private Object tryToLoadFromFile(String filePath, String fileName, boolean zip) {

        if (zip) {
            File zipFile = new File(filePath + ".zip");

            if (zipFile.exists()) {
                try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
                    ZipEntry entry;

                    while ((entry = zin.getNextEntry()) != null) {

                        if (entry.getName().equals(fileName)) {
                            byte[] bytes = getBytes(zin);

                            if (bytes.length == 0) {
                                break;
                            }

                            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                                return ois.readObject();
                            }
                        }
                    }

                } catch (ClassNotFoundException | IOException  e) {
                    System.out.println("Возникла ошибка при загрузке результата из zip файла");
                    e.printStackTrace();
                }
            }

        } else {
            File file = new File(filePath);
            if (file.exists()) {

                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    return ois.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Возникла ошибка при загрузке результата из файла");
                    e.printStackTrace();
                }
            }

        }

        return null;
    }

    private byte[] getBytes(ZipInputStream zin) throws IOException {
        List<Byte> buffer = new ArrayList<>();
        for (int c = zin.read(); c != -1; c = zin.read()) {
            buffer.add((byte) c);
        }

        byte[] bytes = new byte[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            bytes[i] = buffer.get(i);
        }
        return bytes;
    }

    private String getFullName(String prefix, Object[] identityByArgs) {
        StringBuilder sb = new StringBuilder(prefix);

        Arrays.stream(identityByArgs).filter(Objects::nonNull).forEach(o -> sb.append(o.hashCode()));
        return sb.toString();
    }

    private Object[] getIdentityByArgs(Object[] args, Class[] identityBy, Method method) {
        if (args == null) {
            return new Object[0];
        }

        Object[] identityByArgs = new Object[identityBy.length];

        for (int i = 0, j = 0; i < identityBy.length; i++) {
            for (; j < args.length; j++) {
                if (args[j].getClass() == identityBy[i]) {
                    identityByArgs[i] = args[i];
                    break;
                }
            }
        }

        if (Arrays.stream(identityByArgs).noneMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("В аннотации @Cache у метода " + method.getName() + " в графе "
                    + "identityBy не указан ни один тип передаваемого аргумента, либо указан класс примитива. "
                    + "Вместо примитивного класса необходимо указать класс обертку, а вместо типа интерфейса"
                    + " тип конкретного класса");
        }

        return identityByArgs;
    }


}