package ru.sberbank.school.task07;

import java.io.FileNotFoundException;

/**
 * Задание: Верните из метода строки файла, номера которых передаются в метод В качестве T выберите
 * наиболее подходящую коллекцию.
 */
public interface AnyStringNumber<T> {

    //описание в голове интерфейса
    T findStringsByNumbers(String pathToFile, Integer... numbers) throws FileNotFoundException;

}
