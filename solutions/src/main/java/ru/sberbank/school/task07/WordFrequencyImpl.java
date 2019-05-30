package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyImpl implements WordFrequency<Map<String, Integer>> {

    private final FileParser fileParser;

    public WordFrequencyImpl(@NonNull FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Override
    public Map<String, Integer> countWords(@NonNull String pathToFile) throws FileNotFoundException {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> listFromFile = fileParser.parse(pathToFile);
        for (String s : listFromFile) {
            String[] stringArray = s.split("\\s+");
            for (String key : stringArray) {
                Integer value = resultMap.getOrDefault(key, 0);
                resultMap.put(key, ++value);
            }
        }

        return resultMap;
    }
}
