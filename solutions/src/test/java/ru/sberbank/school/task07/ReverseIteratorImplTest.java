package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ReverseIteratorImplTest {
    private final CollectionsServiceFactory factory = new CollectionsServiceFactoryImpl();
    private List<String> strings;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        strings =  factory.getFileParser().parse("f:/temp/test.txt");
    }

    @Test
    void hasNextReturnsFalseInTimeAndNextMovesPointer() {
        ReverseOrderIterator<String> iterator = factory.getReversOrderIterator(strings);
        for (int i = 0; i < strings.size(); i++) {
            iterator.next();
        }
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    void nextOnFirstElementReturnsLastEntry() {
        ReverseOrderIterator<String> iterator = factory.getReversOrderIterator(strings);
        String fromIterator = iterator.next();
        String fromGetter = strings.get(strings.size() - 1);
        Assertions.assertEquals(fromGetter, fromIterator);
    }

    @Test
    void nextOnLastElementReturnsFirstEntry() {
        ReverseOrderIterator<String> iterator = factory.getReversOrderIterator(strings);
        for (int i = 0; i < strings.size() - 1; i++) {
            iterator.next();
        }
        String fromIterator = iterator.next();
        String fromGetter = strings.get(0);
        Assertions.assertEquals(fromGetter, fromIterator);
    }


}