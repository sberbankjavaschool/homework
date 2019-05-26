package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CounterImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private final CounterImpl counter = new CounterImpl(fileParser);

    @Test
    public void countTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        int result = 0;
        int target = 6;
        try {
            result = counter.count(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(target, result);
    }
}