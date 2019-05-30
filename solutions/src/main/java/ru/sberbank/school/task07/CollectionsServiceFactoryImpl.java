package ru.sberbank.school.task07;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public AnyStringNumber<List<String>> getAnyStringNumber(FileParser fileParser) {
        return new AnyStringNumberImpl(fileParser);
    }

    @Override
    public DifferentWords<Set<String>> getDifferentWords(FileParser fileParser) {
        return new DifferentWordsImpl(fileParser);
    }

    @Override
    public <E> ReverseOrderIterator<E> getReversOrderIterator(List<E> elements) {
        return new ReverseIteratorImpl<>(elements);
    }

    @Override
    public WordFrequency<Map<String, Integer>> getWordFrequency(FileParser fileParser) {
        return new WordFrequencyImpl(fileParser);
    }

    public ReverseOrder<List<String>> getReverseOrder(FileParser fileParser) {
        return new ReverseOrderImpl(fileParser);
    }
}
