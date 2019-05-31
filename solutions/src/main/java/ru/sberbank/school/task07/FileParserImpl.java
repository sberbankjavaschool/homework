package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(@NonNull String pathToFile) throws FileNotFoundException {

        File file = new File(pathToFile);

        List<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            list.add(scanner.nextLine().toLowerCase());
        }

        return list;
    }
}
