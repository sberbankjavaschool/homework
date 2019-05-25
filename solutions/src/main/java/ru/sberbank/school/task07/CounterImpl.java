package ru.sberbank.school.task07;

import lombok.NonNull;
import ru.sberbank.school.task07.utill.LineParser;

import java.io.FileNotFoundException;
import java.util.*;

public class CounterImpl implements Counter {
    private FileParser parser;

    public CounterImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public int count(String pathToFile) throws FileNotFoundException {
        Set<String> set = new HashSet<>();
        List<String> list = parser.parse(pathToFile);
        for (String l : list) {
            set.addAll(LineParser.getWords(l.toLowerCase()));
        }
        return set.size();
    }
}
