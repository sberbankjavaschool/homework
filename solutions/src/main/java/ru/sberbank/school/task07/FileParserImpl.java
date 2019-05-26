package ru.sberbank.school.task07;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.nio.file.*;
import java.io.IOException;

public class FileParserImpl implements FileParser {

    /**
     * Парсит файл и строки записывает в List. Каждая строка - отдельный элемент листа.
     *
     * @param pathToFile - путь до файла. Может быть как относительным так и абсолютным путем.
     * @return список все строк файла
     */
    public List<String> parse(String pathToFile) {
        List<String> fromFile = new ArrayList<>();
        //long ntime = System.nanoTime();
        try (Stream<String> lines = Files.lines(Paths.get(pathToFile))) {
            lines.forEach(s -> fromFile.add(s.toLowerCase()));
        } catch (IOException ex) {
            System.out.println("Reading file error");
        }
        return fromFile;
    }

}
