package ru.sberbank.school.task13;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class Serializer {
    private final Kryo kryo;
    private final String defaultFilePath;

    Serializer(String defaultFilePath) {
        kryo = new Kryo();
        kryo.register(HashMap.class);
        kryo.register(Object.class);
        this.defaultFilePath = defaultFilePath;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> loadCacheFromFile(String fileName) throws IOException {
        String fullFileName = defaultFilePath + File.separator + fileName + ".method";
        try (InputStream is = new FileInputStream(fullFileName);
                 Input input = new Input(is)) {
            return (Map<String, Object>) kryo.readObject(input, HashMap.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    void saveCacheToFile(String fileName, Map<String, Object> cache) throws IOException {
        String fullFileName = defaultFilePath + File.separator + fileName + ".method";
        try (OutputStream os = new FileOutputStream(fullFileName);
                Output output = new Output(os)) {
            kryo.writeObject(output, cache);
        }
    }

}
