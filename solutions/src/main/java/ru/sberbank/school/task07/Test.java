package ru.sberbank.school.task07;

import java.util.ArrayList;
import java.util.List;

public class Test {

    static String path = "D:\\dev\\java\\SberBank\\homework\\solutions\\src\\main\\java\\ru\\sberbank\\school\\task07\\text.txt";

    public static void main(String[] args) {
        FileParserImpl a = new FileParserImpl();
        CounterImpl b = new CounterImpl(a);
        DifferentWordsImpl c  = new DifferentWordsImpl(a);
        WordsFrequencyImpl d = new WordsFrequencyImpl(a);
        ReverseOrderImpl e = new ReverseOrderImpl(a);
        List<Integer> list = new ArrayList<>();
        list.add(1);
       // list.add(2);
        //list.add(3);

        Integer[] ints = new Integer[]{1, 2, 7, 23};
        ReverseIteratorImpl<Integer> f = new ReverseIteratorImpl<>(list);
       // System.out.println(f.next());
       // System.out.println(f.next());
        AnyStringNumberImpl g = new AnyStringNumberImpl(a);

        try {
            System.out.println(b.count(path));
            System.out.println(c.findSortedDifferentWords(path));
            System.out.println(d.countWords(path));
            System.out.println(e.getReverseOrderedStrings(path));
            System.out.println(g.findStringsByNumbers(path, ints));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
