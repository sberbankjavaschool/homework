package ru.sberbank.school.task07;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(String pathToFile) throws FileNotFoundException {
        Objects.requireNonNull(pathToFile, "Path to file is null");

        List<String> list = new ArrayList<>();
        File file = new File(pathToFile);
        Scanner scanner = new Scanner(file, "UTF-8");

        while (scanner.hasNext()) {
            list.add(scanner.nextLine().toLowerCase());
        }
        return list;
    }
}
