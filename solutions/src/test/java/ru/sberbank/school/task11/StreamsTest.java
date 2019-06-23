package ru.sberbank.school.task11;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class StreamsTest {

    private Person[] array;
    private List<Person> collection;


    @Before
    public void initialize() {
        array = new Person[9];
        collection = new ArrayList<>();
        collection.add(new Person("Jhon", 23));//0
        collection.add(new Person("Jack", 24));//1
        collection.add(new Person("Sam", 18));//2
        collection.add(new Person("James", 25));//3
        collection.add(new Person("Poly", 38));//4
        collection.add(new Person("Mike", 30));//5
        collection.add(new Person("Enjoikin", 33));//6
        collection.add(new Person("Michale", 31));//7
        collection.add(new Person("Tom", 41));//8
        array = collection.toArray(array);
    }

    /**
     * of(Collection<T> elements) + filter + sorted + ToMap
     */
    @Test
    public void streamsTest1() {
        Map<Integer, Person> expected = new HashMap<>();
        expected.put(30, collection.get(5));
        expected.put(31, collection.get(7));
        expected.put(33, collection.get(6));
        expected.put(38, collection.get(4));
        expected.put(41, collection.get(8));

        Map<Integer, Person> actual = Streams.of(collection).
                filter(p -> p.age >= 30).
                sorted((o1, o2) -> (o1.age > o2.age) ? 1 : -1 ).
                toMap(p -> p.age, p -> p);
        Assert.assertEquals(expected, actual);
    }

    /**
     * of(T... elements) + filter + sorted + ToList
     */
    @Test
    public void streamsTest2() {
        List<Person> expected = new ArrayList<>();
        expected.add(array[5]);
        expected.add(array[7]);
        expected.add(array[6]);
        expected.add(array[4]);
        expected.add(array[8]);
        List<Person> actual = Streams.of(array).
                filter(p -> p.age >= 30).
                sorted(((o1, o2) -> (o1.age > o2.age) ? 1 : -1)).
                toList();
        Assert.assertEquals(expected, actual);
    }


    @Setter
    private class Person {

        private int age;
        private String name;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
