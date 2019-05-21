package ru.sberbank.school.task07;


/**
 * Считатель различных слов в файле.
 * <p>
 * Задание: Подсчитайте количество различных слов в файле.
 */
public interface Counter {

    /**
     * Подсчёт различных слов в файле.
     *
     * @param pathToFile путь до файла
     * @return количество различных слов
     */
    int count(String pathToFile);
}
