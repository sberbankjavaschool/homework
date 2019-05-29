package ru.sberbank.school.task07;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(String pathToFile) throws FileNotFoundException {
        Objects.requireNonNull(pathToFile, "No file name provided");
        try (FileInputStream fis = new FileInputStream(pathToFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr)) {
            return br.lines().map(String::toLowerCase).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
