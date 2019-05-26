package ru.sberbank.school.task07;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static ru.sberbank.school.task06.CollectionUtils.newArrayList;

/**
 * 26.05.2019
 * Регистронезависимый парсер
 *
 * @author Gregory Melnikov
 */

public class FileParserImpl implements FileParser {
    /**
     * Парсит файл и строки записывает в List. Каждая строка - отдельный элемент листа.
     *
     * @param pathToFile - путь до файла. Может быть как относительным так и абсолютным путем.
     * @return список все строк файла
     */
    @Override
    public List<String> parse(@NonNull String pathToFile) throws FileNotFoundException {

        List<String> result = newArrayList();

        try (FileReader fileReader = new FileReader(pathToFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.ready()) {
                result.add(bufferedReader.readLine().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}