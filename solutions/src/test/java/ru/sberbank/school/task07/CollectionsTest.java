package ru.sberbank.school.task07;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class CollectionsTest {

    private String pathToFile;
    private CollectionsServiceFactoryImpl csfi;
    private FileParser fileParser;

    @Before
    public void initialize() {
        pathToFile = "./src/test/java/ru/sberbank/school/task07/test.txt";
        csfi = new CollectionsServiceFactoryImpl();
        fileParser = new FileParserImpl();
    }

    @Test
    public void testFileParser() throws FileNotFoundException {
        initialize();
        List<String> stringsFile = csfi.getFileParser().parse(pathToFile);
        Assert.assertNotNull(stringsFile);
        Assert.assertFalse(stringsFile.isEmpty());
    }

    @Test
    public void testCounter() throws FileNotFoundException {
        int count = csfi.getCounter(fileParser).count(pathToFile);
        Assert.assertEquals(30, count);
    }

    @Test
    public void testDifferentWordsImpl() throws FileNotFoundException {
        pathToFile = "./src/test/java/ru/sberbank/school/task07/test2.txt";
        Set set = (Set) csfi.getDifferentWords(fileParser).findSortedDifferentWords(pathToFile);
        List actual = new ArrayList<>(set);
        List expected = Arrays.asList("a", "b", "d", "cd", "dc", "qq", "svfdf");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWordsFrequency() throws FileNotFoundException {
        Map actual = (Map) csfi.getWordFrequency(fileParser).countWords(pathToFile);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("ду", 1); expected.put("ту", 3); expected.put("мся!", 1);
        expected.put("ми", 1); expected.put("ко", 2); expected.put("зна", 2);
        expected.put("по", 2); expected.put("й", 2); expected.put("ва", 2);
        expected.put("да", 2); expected.put("зья", 1); expected.put("дру", 1);
        expected.put("в", 1); expected.put("бавься", 1); expected.put("до", 1);
        expected.put("гла", 1); expected.put("и", 1); expected.put("ои", 1);
        expected.put("тв", 1); expected.put("ноги", 1); expected.put("ятся", 1);
        expected.put("нрав", 1); expected.put("мне", 1); expected.put("за", 3);
        expected.put("а", 1); expected.put("погода", 1); expected.put("дела", 2);
        expected.put("к", 3); expected.put("ка", 3); expected.put("привет", 2);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void testReverseOrderImpl() throws FileNotFoundException {
        pathToFile = "./src/test/java/ru/sberbank/school/task07/test2.txt";
        List actaual = new ReverseOrderImpl().getReverseOrderedStrings(pathToFile);
        List<String> expected = Arrays.asList("qq", "cd", "d dc", "svfdf", "cd", "b a");
        Assert.assertEquals(expected, actaual);
    }

    @Test
    public void testAnyStringNumberImpl() throws FileNotFoundException {
        Integer[] numbers = {2, 5, 8, 12};
        List actual = (List) csfi.getAnyStringNumber(fileParser).findStringsByNumbers(pathToFile, numbers);
        List<String> expected = Arrays.asList("к погода", "ятся ноги", "привет ка", "дру зья");
        Assert.assertEquals(expected, actual);
    }
}
