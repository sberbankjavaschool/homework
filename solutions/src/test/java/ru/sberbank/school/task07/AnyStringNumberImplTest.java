package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AnyStringNumberImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private AnyStringNumberImpl anyStringNumber = new AnyStringNumberImpl(fileParser);

    @Test
    public void findStringsByNumbersPositiveTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        Integer[] numbers = {3, 1};
        List<String> list = new ArrayList<>();
        try {
            list = anyStringNumber.findStringsByNumbers(pathToFile, numbers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<String> target = new ArrayList<>();
        target.add("string4 test text");
        target.add("string2 test text");

        assertThat(list, equalTo(target));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findStringsByNumbersNegativeTest() {
        File file = new File("src/test/resources/task07/CounterTest.txt");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        Integer[] numbers = {3, 1, 11};
        List<String> list = new ArrayList<>();
        try {
            list = anyStringNumber.findStringsByNumbers(pathToFile, numbers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}