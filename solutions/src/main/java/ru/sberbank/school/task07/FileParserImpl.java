package ru.sberbank.school.task07;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileParserImpl implements FileParser {


    @Override
    public List<String> parse(String pathToFile) throws FileNotFoundException {

        Objects.requireNonNull(pathToFile, "Parameter pathToFile must be not null!");

        if (pathToFile.isEmpty()) {
            throw new FileNotFoundException("Parameter pathToFile must be not empty!");
        }

        List<String> list = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(pathToFile), "windows-1251"))) {

            while (reader.ready()) {
                list.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
