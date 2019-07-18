package ru.sberbank.school.task13.cacheProxy.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task13.cacheProxy.exception.CacheProxyException;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SerializableManager {
    private final Kryo kryo;

    public SerializableManager() {
        this.kryo = new Kryo();
        this.kryo.register(String.class);
        this.kryo.register(Object.class);
        this.kryo.register(Class.class);
        this.kryo.register(ArrayList.class);
    }

    public void writeInFile(File file, Object result) throws CacheProxyException {
        try (Output output = new Output(new FileOutputStream(file))) {
            if (result != null) {
                kryo.register(result.getClass());
            }
            kryo.writeClassAndObject(output, result);
        } catch (IOException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }
    public Object readFromFile(File file) throws CacheProxyException {
        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readClassAndObject(input);
        } catch (IOException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }
    public void writeInZip(File file, Object result) throws CacheProxyException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file + ".zip"));
             Output output = new Output(zipOutputStream)) {
            if (result != null) {
                kryo.register(result.getClass());
            }
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            kryo.writeClassAndObject(output, result);
        } catch (IOException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }

    public Object readFromZip(File file) throws CacheProxyException {
        try (Input input = new Input(new ZipInputStream(new FileInputStream(file)))) {
            return kryo.readClassAndObject(input);
        } catch (FileNotFoundException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }
}
