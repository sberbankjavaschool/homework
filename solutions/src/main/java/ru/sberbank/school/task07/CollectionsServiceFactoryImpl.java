package ru.sberbank.school.task07;

import java.util.List;

public class CollectionsServiceFactoryImpl implements CollectionsServiceFactory {
    @Override
    public FileParser getFileParser() {
        return new FileParserImpl();
    }

    @Override
    public Counter getCounter(FileParser fileParser) {
        return new CounterImpl(fileParser);
    }

    @Override
    public AnyStringNumber getAnyStringNumber(FileParser fileParser) {
        return new AnyStringNumberImpl(fileParser);
    }

    @Override
    public DifferentWords getDifferentWords(FileParser fileParser) {
        return new DifferentWordsImpl(fileParser);
    }

    @Override
    public <E> ReverseOrderIterator<E> getReversOrderIterator(List<E> elements) {
        return new ReverseIteratorImpl(elements);
    }

    @Override
    public WordFrequency getWordFrequency(FileParser fileParser) {
        return new WordsFrequencyImpl(fileParser);
    }
}
