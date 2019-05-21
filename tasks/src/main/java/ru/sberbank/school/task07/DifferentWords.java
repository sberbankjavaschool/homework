package ru.sberbank.school.task07;

/**
 * Задание: Соберите список различных слов файла, отсортированный по возрастанию их длины
 * (компаратор сначала по длине слова, потом по тексту). В качестве T выберете наиболее подходящую
 * коллекцию.
 */
public interface DifferentWords<T> {

    //описание в голове интерфейса
    T findSortedDifferentWords(String pathToFile);

}
