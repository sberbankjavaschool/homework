package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReverseOrderImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private final ReverseOrderImpl reverseOrder = new ReverseOrderImpl(fileParser);

    @Test
    public void getReverseOrderedStringsTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        List<String> result = null;
        try {
            result = reverseOrder.getReverseOrderedStrings(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String target = "string4 test text";

        assertEquals(target, result.get(0));

        for (String string : result) {
            System.out.println(string);
        }
    }
}