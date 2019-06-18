package ru.sberbank.school.task13.cacheproxy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CacheSerializatorManager {
    private Kryo kryo = new Kryo();

    public void writeMethodToFile(File file, Object methodRes) throws FileNotFoundException {
        try (FileOutputStream fos = new FileOutputStream(file);
             Output out = new Output(fos)) {
            if (methodRes != null) {
                kryo.register(methodRes.getClass());
            }
            kryo.writeClassAndObject(out, methodRes);
        } catch (NullPointerException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found");
        } catch (KryoException | IOException e) {
            e.printStackTrace();
            System.out.println("Can't serialize this method to file");
        }
    }

    public void writeMethodToZip(File file, Object methodRes) throws FileNotFoundException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
             Output out = new Output(zos)) {
            if (methodRes != null) {
                kryo.register(methodRes.getClass());
            }
            zos.putNextEntry(new ZipEntry(file.getName().replace(".zip", ".txt")));
            kryo.writeClassAndObject(out, methodRes);
        } catch (NullPointerException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found");
        } catch (KryoException | IOException e) {
            e.printStackTrace();
            System.out.println("Can't serialize this method to ZIP");
        }
    }

    public Object loadMethodFromFile(File file) throws FileNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             Input in = new Input(fis)) {
            return kryo.readClassAndObject(in);
        } catch (NullPointerException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found");
        } catch (KryoException | IOException e) {
            e.printStackTrace();
            System.out.println("Can't serialize this method from file");
        }
        return null;
    }

    public Object loadMethodFromZip(File file) throws FileNotFoundException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
             Input in = new Input(zis)) {
            zis.getNextEntry();
            return kryo.readClassAndObject(in);
        } catch (NullPointerException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found");
        } catch (KryoException | IOException e) {
            e.printStackTrace();
            System.out.println("Can't serialize this method from ZIP");
        }
        return null;
    }


}
