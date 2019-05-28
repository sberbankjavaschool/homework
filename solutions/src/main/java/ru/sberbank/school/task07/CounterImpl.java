package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

public class CounterImpl implements Counter {

    private FileParser parser;

    public CounterImpl(FileParser parser) {
        Objects.requireNonNull(parser, "Parameter parser must be not null!");
        this.parser = parser;
    }


    @Override
    public int count(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Set<String> setWord = new HashSet<>();

        for (String str : list) {
            String[] words = str.toLowerCase().split("\\s+");
            setWord.addAll(Arrays.asList(words));
        }
        return setWord.size();
    }
}
