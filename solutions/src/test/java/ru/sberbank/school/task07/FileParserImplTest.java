package ru.sberbank.school.task07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileParserImplTest {
    private final FileParser fp = new FileParserImpl();

    @Test
    void parseWithNoFilenameThrowsNpe() {
        Assertions.assertThrows(NullPointerException.class, () -> fp.parse(null));
    }

    @Test
    void parseWithWrongFilenameThrowsFnfe() {
        Assertions.assertThrows(FileNotFoundException.class, () -> fp.parse(""));
    }

    @Test
    void parseReturnsNonEmptyList() throws FileNotFoundException {
        List<String> list = fp.parse("f:/temp/test.txt");
        System.out.println(list.toString());
        Assertions.assertFalse(list.isEmpty());
    }
}