package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class CounterImpl implements Counter {
    private FileParser parser;

    public CounterImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Парсер не может быть null");
        this.parser = parser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        List<String> srcStrings = parser.parse(pathToFile);
        Set<String> resultSet = new HashSet<>();

        for (String string : srcStrings) {
            String[] temp = string.replaceAll("\\p{Punct}", " ")
                    .trim().split("\\s");

            for (String word : temp) {
                if (word.length() > 0) {
                    resultSet.add(word);
                }
            }
        }

        return resultSet.size();
    }
}
