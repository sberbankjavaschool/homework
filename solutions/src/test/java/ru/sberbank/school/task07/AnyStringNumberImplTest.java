package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

class AnyStringNumberImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final AnyStringNumber<List<String>> parser = new AnyStringNumberImpl(fileParser);

    @Test
    void findStringsByNumbersWorks() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("zero");
        strings.add("one");
        strings.add("two");
        strings.add("three");
        Mockito.when(fileParser.parse(Mockito.anyString())).thenReturn(strings);
        List<String> wordMap = parser.findStringsByNumbers("some path", 2, 3);
        Assertions.assertAll(
                () -> Assertions.assertEquals("two", wordMap.get(0)),
                () -> Assertions.assertEquals("three", wordMap.get(1))
        );
    }
}