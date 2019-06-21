package ru.sberbank.school.task13.cacheproxy.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task13.cacheproxy.exceptions.CachingSerializationException;

import java.io.*;

public class CachingSerializer {
    private final Kryo kryo = new Kryo();
    private final String path;
    private Class objectClass;

    public CachingSerializer(@NonNull String path) {
        this.path = path;
    }

    public void initialize(Class objectClass) {
        this.objectClass = objectClass;
        kryo.register(objectClass);
    }

    public void writeToFile(@NonNull String key, Object object) {
        String fullName = path + File.separator + key + ".cache";

        try (FileOutputStream fos = new FileOutputStream(fullName);
                       Output out = new Output(fos)) {
            kryo.writeObject(out, object);
        } catch (FileNotFoundException e) {
            throw new CachingSerializationException("Не найденна директория, проверьте правильность пути", e);
        } catch (IOException e) {
            throw new CachingSerializationException("Не удается сериализовать", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Object readFromFile(@NonNull String key) {
        String fullName = path + File.separator + key + ".cache";

        try (FileInputStream fis = new FileInputStream(fullName);
                        Input in = new Input(fis)) {
            return kryo.readObject(in, objectClass);
        } catch (FileNotFoundException e) {
            throw new CachingSerializationException("Не найденна директория, проверьте правильность пути", e);
        } catch (IOException e) {
            throw new CachingSerializationException("Не удается десериализовать", e);
        }
    }
}
