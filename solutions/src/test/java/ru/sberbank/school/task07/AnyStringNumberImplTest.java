package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class AnyStringNumberImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final AnyStringNumber<Map<Integer, String>> parser = new AnyStringNumberImpl(fileParser);

    @Test
    void findStringsByNumbersWorks() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("zero");
        strings.add("one");
        strings.add("two");
        strings.add("three");
        Mockito.when(fileParser.parse(Matchers.anyString())).thenReturn(strings);
        Map<Integer, String> wordMap = parser.findStringsByNumbers("some path", 2, 3);
        Assertions.assertAll(
                () -> Assertions.assertEquals("two", wordMap.get(2)),
                () -> Assertions.assertEquals("three", wordMap.get(3))
        );
    }
}