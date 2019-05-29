package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReverseOrderImplTest {
    private final FileParser fileParser = Mockito.mock(FileParserImpl.class);
    private final ReverseOrder<List<String>> parser = new ReverseOrderImpl(fileParser);


    @Test
    void getReverseOrderedStringsReturnsReversedOrderStrings() throws FileNotFoundException {
        List<String> strings = new ArrayList<>();
        strings.add("zero");
        strings.add("one");
        strings.add("two");
        strings.add("three");
        Mockito.when(fileParser.parse(Matchers.anyString())).thenReturn(strings);

        List<String> straightOrder = fileParser.parse("some path");
        Collections.reverse(straightOrder);

        List<String> reversedOrder = parser.getReverseOrderedStrings("some path");
        Assertions.assertEquals(straightOrder, reversedOrder);
    }
}