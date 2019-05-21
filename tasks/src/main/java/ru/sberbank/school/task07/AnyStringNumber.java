package ru.sberbank.school.task07;

/**
 * Задание: Верните из метода строки файла, номера которых передаются в метод В качестве T выберете
 * наиболее подходящую коллекцию.
 */
public interface AnyStringNumber<T> {

    //описание в голове интерфейса
    T findStringsByNumbers(Integer... numbers);

}
