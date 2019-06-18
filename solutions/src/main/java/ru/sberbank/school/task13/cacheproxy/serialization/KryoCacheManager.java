package ru.sberbank.school.task13.cacheproxy.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task13.cacheproxy.exception.SerializationInCacheException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class KryoCacheManager {
    private Kryo kryo = new Kryo();
    private String rootPath;
    private Class classObject;

    public KryoCacheManager(@NonNull String rootPath) {
        this.rootPath = rootPath;
    }

    public void register(@NonNull Class classObject) {
        this.classObject = classObject;
        kryo.register(classObject);
    }

    public void saveObject(String name, Object object) {
        Objects.requireNonNull(name, "File name is not to be null");

        String path = rootPath + "/" + name;

        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
                Output output = new Output(fileOutputStream)) {

            kryo.writeObject(output, object);

        } catch (FileNotFoundException e) {
            throw new SerializationInCacheException("The path was not found. Check the path to the root folder", e);
        } catch (IOException e) {
            throw new SerializationInCacheException("Fail or interrupt I/O operations", e);
        }
    }

    public Object loadObject(String name) {
        Objects.requireNonNull(name, "File name is not to be null");

        String path = rootPath + "/" + name;

        try (FileInputStream fileInputStream = new FileInputStream(path);
                Input input = new Input(fileInputStream)) {

            return kryo.readObject(input, classObject);

        } catch (FileNotFoundException e) {
            throw new SerializationInCacheException("The path was not found. Check the path to the root folder", e);
        } catch (IOException e) {
            throw new SerializationInCacheException("Fail or interrupt I/O operations", e);
        }
    }
}
