package ru.sberbank.school.task07;

import java.util.List;

public class CollectionsServiceFactoryImpl implements CollectionsServiceFactory {
    /**
     * Возвращает инстанс FileParser реализованнную студентом.
     *
     * @return объект - парсер файла
     */
    public FileParser getFileParser() {
        return new FileParserImpl();
    }

    public Counter getCounter(FileParser fileParser) {
        return new CounterImpl(fileParser);
    }

    public AnyStringNumber getAnyStringNumber(FileParser fileParser) {
        return new AnyStringNumberImpl(fileParser);
    }

    public DifferentWords getDifferentWords(FileParser fileParser) {
        return new DifferentWordsImpl(fileParser);
    }

    public <E> ReverseOrderIterator<E> getReversOrderIterator(List<E> elements) {
        return new ReverseIteratorImpl<>(elements);
    }

    public WordFrequency getWordFrequency(FileParser fileParser) {
        return new WordsFrequencyImpl(fileParser);
    }
}
