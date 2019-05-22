package ru.sberbank.school.task07;

import java.io.FileNotFoundException;

/**
 * Задание: Соберите все строки файла в обратном порядке и верните их в подходящей коллекции. В
 * качестве T выберите наиболее подходящую коллекцию.
 */
public interface ReverseOrder<T> {

    //описание в голове интерфейса
    T getReverseOrderedStrings(String pathToFile) throws FileNotFoundException;

}
