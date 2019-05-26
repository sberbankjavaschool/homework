package ru.sberbank.school.task07;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String path = "C:\\Users\\Anastasia\\Desktop\\Java\\Tasks\\homework\\solutions\\src\\main\\java\\" +
                "ru\\sberbank\\school\\task07\\test.txt";
        FileParser fileParser = new FileParserImpl();
        try {
            List<String> stringsFromFile = fileParser.parse(path);
            System.out.println(stringsFromFile);
            Counter counter = new CounterImpl(fileParser);
            int difWords = counter.count(path);
            System.out.println("====================");

            DifferentWordsImpl differentWords = new DifferentWordsImpl(fileParser);
            List<String> sortedListDifWords = differentWords.findSortedDifferentWords(path);
            System.out.println(sortedListDifWords);
            System.out.println("====================DW");

            WordsFrequencyImpl wordsFrequency = new WordsFrequencyImpl(fileParser);
            Map<String, Integer> wordsFrequencyMap = wordsFrequency.countWords(path);
            System.out.println(wordsFrequencyMap);
            System.out.println("====================WF");

            ReverseOrderImpl revOrder = new ReverseOrderImpl(fileParser);
            List<String> reverseOrderedStrings = revOrder.getReverseOrderedStrings(path);
            System.out.println(stringsFromFile);
            System.out.println(reverseOrderedStrings);
            System.out.println("====================RO");

            AnyStringNumberImpl anyStringNumber = new AnyStringNumberImpl(fileParser);
            List<String> stringsByNumbers = anyStringNumber.findStringsByNumbers(path, 1, 4, 22, 5, 2, 5);
            System.out.println(stringsByNumbers);
            System.out.println("====================AS");


        } catch (FileNotFoundException ex) {

        }


    }
}
