package ru.sberbank.school.task07;

import ru.sberbank.school.task08.state.MapState;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsFrequencyImpl implements WordFrequency {

    private FileParser parser;

    public WordsFrequencyImpl(FileParser parser) {
        this.parser = parser;
    }

    @Override
    public Object countWords(String pathToFile) throws FileNotFoundException {

        List<String> list = parser.parse(pathToFile);
        Map<String, Integer> countWord = new HashMap<>();

        for (String str : list) {

            String[] words = str.toLowerCase().split("\\s+");

            for (String word : words) {

                Integer count = countWord.getOrDefault(word.toLowerCase(), 0);
                countWord.put(word, ++ count);
            }
        }

        return countWord;
    }
}
