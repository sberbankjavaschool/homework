package ru.sberbank.school.task07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(String pathToFile) throws FileNotFoundException {
        Objects.requireNonNull(pathToFile, "Путь к файлу не может быть null");

        if (pathToFile.isEmpty()) {
            throw new IllegalArgumentException("Не указан путь к файлу");
        }

        File text = new File(pathToFile);
        Scanner sc = new Scanner(text);

        List<String> strings = new ArrayList<>();

        while (sc.hasNext()) {
            strings.add(sc.nextLine().toLowerCase());
        }

        return strings;
    }
}
