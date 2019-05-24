package ru.sberbank.school.task07;

import java.util.List;

/**
 * Фабрика по инициализации объектов для задач по коллекциям.
 */
public interface CollectionsServiceFactory {
    /**
     * Возвращает инстанс FileParser реализованнную студентом.
     *
     * @return объект - парсер файла
     */
    FileParser getFileParser();

    Counter getCounter(FileParser fileParser);

    AnyStringNumber getAnyStringNumber(FileParser fileParser);

    DifferentWords getDifferentWords(FileParser fileParser);

    <E> ReverseOrderIterator<E> getReversOrderIterator(List<E> elements);

    WordFrequency getWordFrequency(FileParser fileParser);
}
