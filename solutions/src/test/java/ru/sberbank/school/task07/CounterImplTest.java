package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

class CounterImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final Counter counter = new CounterImpl(fileParser);

    @Test
    void counterCountProperly() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("zero");
        strings.add("one");
        strings.add("two");
        strings.add("three");
        Mockito.when(fileParser.parse(Matchers.anyString())).thenReturn(strings);

        Assertions.assertEquals(4, counter.count("some path"));
    }
}