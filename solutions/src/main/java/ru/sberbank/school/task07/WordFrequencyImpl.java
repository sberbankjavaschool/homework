package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyImpl implements WordFrequency {

    private final FileParser fileParser;
    private final Map<String, Integer> map = new HashMap<>();

    public WordFrequencyImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {

        List<String> list = fileParser.parse(pathToFile);
        list.stream()
                .map(s -> s.split("[^a-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(strings -> !strings.equals(""))
                .forEach(this::add);
        return map;
    }

    private void add(String o) {
        Integer count = map.get(o);
        map.put(o, count == null ? 1 : count + 1);
    }
}
