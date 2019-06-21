package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyImpl implements WordFrequency {

    private final FileParser fileParser;


    public WordFrequencyImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        Map<String, Integer> map = new HashMap<>();
        List<String> list = fileParser.parse(pathToFile);
        list.stream()
                .map(s -> s.split("[^a-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(strings -> !strings.equals(""))
                .forEach(s -> {
                    Integer count = map.get(s);
                    map.put(s, count == null ? 1 : count + 1);
                });
        return map;
    }

}
