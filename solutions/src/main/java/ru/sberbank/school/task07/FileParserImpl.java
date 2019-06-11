package ru.sberbank.school.task07;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(String pathToFile) {
        List<String> list = null;
        try {
           list = Files.lines(Paths.get(pathToFile), StandardCharsets.UTF_8)
                   .map(String::toLowerCase)
                   .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
