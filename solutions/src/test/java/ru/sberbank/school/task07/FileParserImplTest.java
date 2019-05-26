package ru.sberbank.school.task07;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.sberbank.school.task06.CollectionUtils.newArrayList;

public class FileParserImplTest {

    private final FileParser fileParser = new FileParserImpl();

    @Test
//    @SneakyThrows
    public void parseTest() {

        File file = new File("src/test/resources/task07/readme.md");
        String pathToFile = file.getAbsolutePath();

        System.out.println(pathToFile);

        List<String> list = newArrayList();
        try {
            list = fileParser.parse(pathToFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String target = "именования файлов в модуле solutions должны быть строго такими, как указано в названии.";

        assertEquals(target, list.get(31));

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ". " + list.get(i));
        }
    }
}