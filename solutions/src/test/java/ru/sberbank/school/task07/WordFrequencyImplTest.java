package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordFrequencyImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private final WordFrequency<Map<String, Integer>> wordFrequency = new WordsFrequencyImpl(fileParser);

    @Test
    public void countWordsTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        Map<String, Integer> result = null;
        try {
            result = wordFrequency.countWords(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int count = 4;
        String target = "тестовый";
        assertEquals(Integer.valueOf(count), result.get(target));

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " раз");
        }
    }
}