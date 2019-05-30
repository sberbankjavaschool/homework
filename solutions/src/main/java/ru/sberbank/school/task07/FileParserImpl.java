package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParserImpl implements FileParser {

    /**
     * Парсит файл и строки записывает в List. Каждая строка - отдельный элемент листа.
     *
     * @param pathToFile - путь до файла. Может быть как относительным так и абсолютным путем.
     * @return список все строк файла
     */
    @Override
    public List<String> parse(@NonNull String pathToFile) throws FileNotFoundException {

        if (pathToFile.isEmpty()) {
            throw new FileNotFoundException("Передан пустой путь");
        }

        List<String> resultList = new ArrayList<>();

        try (FileReader fr = new FileReader(new File(pathToFile));
                BufferedReader reader = new BufferedReader(fr)) {
            String line = reader.readLine();
            while (line != null) {
                resultList.add(line.toLowerCase());
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при чтении файла");
        }
        return resultList;
    }
}
