package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Задание: Подсчитайте сколько раз каждое слово встречается в файле. В качестве T выберите наиболее
 * подходящую коллекцию.
 */
public class WordsFrequencyImpl implements WordFrequency {
    private FileParser fileParser;

    public WordsFrequencyImpl(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public Map<String, Integer> countWords(String pathToFile) throws FileNotFoundException {
        Map<String, Integer> wordsFrequencyMap = new HashMap<>();
        List<String> wordsFromFile = fileParser.parse(pathToFile);
        for (String word : wordsFromFile) {
            for (String s : Arrays.asList(word.toLowerCase().trim().split("\\s"))) {
                wordsFrequencyMap.put(s, wordsFrequencyMap.getOrDefault(word, 0) + 1);
            }
        }
        return wordsFrequencyMap;
    }
}
