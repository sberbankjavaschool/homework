package ru.sberbank.school.task07;

import java.util.List;

/**
 * Интерфейс парсера файла.
 * <p>
 * Задание: Реализовать интерфейс FileParser, который должен уметь считывать строки с файла и
 * записывать их в List.
 */
public interface FileParser {

    /**
     * Парсит файл и строки записывает в List. Каждая строка - отдельный элемент листа.
     *
     * @param pathToFile - путь до файла. Может быть как относительным так и абсолютным путем.
     * @return список все строк файла
     */
    List<String> parse(String pathToFile);

}
