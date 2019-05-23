package ru.sberbank.school.task07;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileParserImpl implements FileParser {

    @Override
    public List<String> parse(String pathToFile) throws FileNotFoundException {

        Objects.requireNonNull(pathToFile, "Путь к файлу не должен быть null");

        if (pathToFile.isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не должен быть задан пустой строкой");
        }

        BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
        List<String> list = new ArrayList<>();

        try {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
