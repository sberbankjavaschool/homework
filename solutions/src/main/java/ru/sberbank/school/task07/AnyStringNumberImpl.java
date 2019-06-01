package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class AnyStringNumberImpl implements AnyStringNumber<List<String>> {
    private FileParser parser;

    public AnyStringNumberImpl(FileParser parser) {
        Objects.requireNonNull(parser, "FileParser cannot bo null");

        this.parser = parser;
    }

    @Override
    public List<String> findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException {
        List<String> list = parser.parse(pathToFile);
        List<String> result = new ArrayList<>();

        for (Integer index : numbers) {
            if (index > 0 && index <= list.size()) {
                result.add(list.get(index - 1));
            }
        }
        return result;
    }
}
