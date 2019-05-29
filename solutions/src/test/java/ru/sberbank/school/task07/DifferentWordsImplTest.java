package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Mockito.when(fileParser.parse(Matchers.anyString())).thenReturn(strings);

        List<String> expectedWords = Arrays.asList("zero", "one", "two", "three");
        List<String> words = counter.findSortedDifferentWords("some path");

        Assertions.assertAll(
                () -> Assertions.assertTrue(expectedWords.containsAll(words)),
                () -> Assertions.assertTrue(words.containsAll(expectedWords))
        );
    }
}