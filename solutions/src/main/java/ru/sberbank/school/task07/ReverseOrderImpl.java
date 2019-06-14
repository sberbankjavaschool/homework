package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder<List> {
    @Override
    public List getReverseOrderedStrings(@NonNull String pathToFile) throws FileNotFoundException {
        List<String> stringsFile = new FileParserImpl().parse(pathToFile);
        Collections.reverse(stringsFile);
        return stringsFile;
    }
}
