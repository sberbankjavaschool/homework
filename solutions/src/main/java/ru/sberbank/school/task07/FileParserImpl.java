package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParserImpl implements FileParser {

    @Override
    public List<String> parse(@NonNull String pathToFile) throws FileNotFoundException {
        if (pathToFile.isEmpty()) {
            throw new FileNotFoundException("Path is not to be empty");
        }

        List<String> list = new ArrayList<>();

        try (FileReader fileReader = new FileReader(pathToFile);
                BufferedReader reader = new BufferedReader(fileReader)) {

            while (reader.ready()) {
                list.add(reader.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
