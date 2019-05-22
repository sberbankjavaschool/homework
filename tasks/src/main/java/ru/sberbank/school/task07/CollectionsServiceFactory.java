package ru.sberbank.school.task07;

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

    <E> ReverseOrderIterator<E> getReversOrderIterator(FileParser fileParser);

    WordFrequency getWordFrequency(FileParser fileParser);
}
