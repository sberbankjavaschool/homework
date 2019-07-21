package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(@NonNull String pathToFile) {
        List<String> result = new ArrayList<>();
        File file = new File(pathToFile);
        if (!file.canRead() || file.isDirectory()) {
            throw new IllegalArgumentException("Can't get access to the file " + pathToFile);
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                result.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found " + pathToFile);
        }
        return result;
    }
}
