package ru.sberbank.school.task13.cacheproxy.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task13.cacheproxy.exception.CacheProxyException;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SerializationManager {
    private final Kryo kryo;

    public SerializationManager() {
        this.kryo = new Kryo();
        this.kryo.register(String.class);
        this.kryo.register(Object.class);
        this.kryo.register(Class.class);
        this.kryo.register(ArrayList.class);
    }

    public void writeMethodInFile(File file, Object result) throws CacheProxyException {
        try (Output output = new Output(new FileOutputStream(file))) {
            if (result != null) {
                kryo.register(result.getClass());
            }
            kryo.writeClassAndObject(output, result);
        } catch (KryoException | FileNotFoundException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }

    public Object readMethodFromFile(File file) throws CacheProxyException {
        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readClassAndObject(input);
        } catch (FileNotFoundException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }

    public void writeInZip(File file, Object result) throws CacheProxyException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file + ".zip"));
                Output output = new Output(zos)) {
            if (result != null) {
                kryo.register(result.getClass());
            }
            ZipEntry entry1 = new ZipEntry(file.getName());
            zos.putNextEntry(entry1);
            kryo.writeClassAndObject(output, result);
        } catch (IOException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }

    public Object readMethodFromZip(File file) throws CacheProxyException {
        try (Input input = new Input(new ZipInputStream(new FileInputStream(file)))) {
            return kryo.readClassAndObject(input);
        } catch (FileNotFoundException e) {
            throw new CacheProxyException(e.getMessage());
        }
    }
}
