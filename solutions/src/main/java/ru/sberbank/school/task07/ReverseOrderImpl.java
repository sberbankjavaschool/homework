package ru.sberbank.school.task07;

import java.util.Collections;
import java.util.List;

public class ReverseOrderImpl implements ReverseOrder<List<String>> {
    @Override
    public List<String> getReverseOrderedStrings(String pathToFile) {
        List<String> result;
        FileParserImpl parser = new FileParserImpl();
        result = parser.parse(pathToFile);
        Collections.reverse(result);
        return result;
    }
}
