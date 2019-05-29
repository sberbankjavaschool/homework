package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class WordsFrequencyImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final WordFrequency<Map<String, Integer>> counter = new WordsFrequencyImpl(fileParser);

    @Test
    void countWordsCounts() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("three, zero . one // two");
        strings.add("one two three");
        strings.add("two three");
        strings.add("three");
        Mockito.when(fileParser.parse(Mockito.anyString())).thenReturn(strings);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("three", 4);
        expected.put("two", 3);
        expected.put("one", 2);
        expected.put("zero", 1);

        Assertions.assertEquals(expected, counter.countWords("some path"));
    }
}