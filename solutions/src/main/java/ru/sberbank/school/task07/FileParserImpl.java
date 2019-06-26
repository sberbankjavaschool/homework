package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParserImpl implements FileParser {
    @Override
    public List<String> parse(@NonNull String pathToFile) throws FileNotFoundException {
        List<String> stringsFile = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile));

        try {
            while (bufferedReader.ready()) {
                stringsFile.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringsFile;
    }
}
