package ru.sberbank.school.task07;

import java.io.FileNotFoundException;

/**
 * Задание: Подсчитайте сколько раз каждое слово встречается в файле. В качестве T выберите наиболее
 * подходящую коллекцию.
 */
public interface WordFrequency<T> {

    //описание в голове интерфейса
    T countWords(String pathToFile) throws FileNotFoundException;

}
