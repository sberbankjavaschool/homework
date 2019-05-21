package ru.sberbank.school.task07;

/**
 * Задание: Подсчитайте сколько раз каждое слово встречается в файле. В качестве T выберете наиболее
 * подходящую коллекцию.
 */
public interface WordFrequency<T> {

    //описание в голове интерфейса
    T countWords(String pathToFile);

}
