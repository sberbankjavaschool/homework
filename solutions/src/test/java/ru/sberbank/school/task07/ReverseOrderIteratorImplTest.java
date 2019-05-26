package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReverseOrderIteratorImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private ReverseIteratorImpl reverseOrderIterator = null;

    @Test
    public void reverseOrderIteratorTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        List<String> list = new ArrayList<>();
        try {
            list = fileParser.parse(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        reverseOrderIterator = new ReverseIteratorImpl(list);

        String target = "строка4 тестовый текст";

        assertEquals(target, reverseOrderIterator.next());

        while (reverseOrderIterator.hasNext()) {
            System.out.println(reverseOrderIterator.next());
        }
    }
}