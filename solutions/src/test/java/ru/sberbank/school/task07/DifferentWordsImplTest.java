package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.*;

class DifferentWordsImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final DifferentWordsImpl counter = new DifferentWordsImpl(fileParser);

    @Test
    void sorterSorts() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("three, zero . one // two");
        strings.add("one two three");
        strings.add("one two");
        strings.add("three");
        Mockito.when(fileParser.parse(Mockito.anyString())).thenReturn(strings);

        String expectedWords = Arrays.asList("one", "two", "zero", "three").toString();
        Set<String> words = counter.findSortedDifferentWords("some path");

        Assertions.assertEquals(expectedWords, words.toString());
    }
}