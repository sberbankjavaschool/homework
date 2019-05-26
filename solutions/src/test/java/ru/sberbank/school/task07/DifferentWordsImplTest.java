package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DifferentWordsImplTest {

    private final FileParser fileParser = new FileParserImpl();

    private final DifferentWordsImpl differentWords = new DifferentWordsImpl(fileParser);

    @Test
    public void findSortedDifferentWordsTest() {
        File file = new File("src/test/resources/task07/readme.md");
        String pathToFile = file.getAbsolutePath();
        System.out.println(pathToFile);

        Set<String> result = new TreeSet<>();

        try {
            result = differentWords.findSortedDifferentWords(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> checkList = Arrays.asList("такими", "тексту", "только", "файлов", "верните", "задание");
        List<String> subList = new ArrayList<>(((TreeSet<String>) result)
                .subSet("такими", true, "задание", true));

        assertThat(subList, equalTo(checkList));

        Iterator<String> iterator = result.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            System.out.println(i + ". " + iterator.next());
        }
    }
}