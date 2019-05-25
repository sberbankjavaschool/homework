package ru.sberbank.school.task07;

import lombok.NonNull;
import ru.sberbank.school.task07.utill.LineParser;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsFrequencyImpl implements WordFrequency {
    private FileParser parser;

    public WordsFrequencyImpl(@NonNull FileParser fileParser) {
        this.parser = fileParser;
    }

    @Override
    public Map<String,Integer> countWords(String pathToFile) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);

        Map<String,Integer> map = new HashMap<>();
        for (String l : list) {
            for (String w : LineParser.getWords(l.toLowerCase())) {
                map.put(w, map.getOrDefault(w, 0) + 1);
            }
        }

        return map;
    }
}
