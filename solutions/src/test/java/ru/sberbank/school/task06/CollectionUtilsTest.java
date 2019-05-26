package ru.sberbank.school.task06;

import lombok.Getter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.sberbank.school.task06.CollectionUtils.*;

/**
 * Тестирование работы статических методов {@link CollectionUtils}
 *
 * @author Gregory Melnikov, created on 25.05.19
 */

public class CollectionUtilsTest {

    @Test
    public void addAllTest() {
        List<String> strings = Arrays.asList("string1", "string2", "string3");
        List<String> copyOfStrings = new ArrayList<>();
        addAll(strings, copyOfStrings);
        assertThat(copyOfStrings, equalTo(strings));
    }

    @Test
    public void indexOfTest() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());

        int expect = plants.size();
        Oak oak = new Oak();
        plants.add(oak);

        int result = indexOf(plants, oak);
        assertEquals(expect, result);
    }

    @Test
    public void limitTest() {
        int size = 2;
        List<String> strings = Arrays.asList("string1", "string2", "string3");
        List<String> result = limit(strings, size);
        assertEquals(size, result.size());
    }

    @Test
    public void limitTestSizeOutOfBounds() {
        int size = 10;
        List<String> strings = Arrays.asList("string1", "string2", "string3");
        List<String> result = limit(strings, size);
        assertEquals(strings.size(), result.size());
    }

    @Test
    public void addTest() {
        List<Plant> list = new ArrayList<>();
        add(list,new Tree());
        add(list,new Oak());
        assertEquals(2, list.size());
    }

    @Test
    public void removeAllTest() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());

        Oak expectedTarget = new Oak();
        plants.add(expectedTarget);

        List<Tree> toRemove = new ArrayList<>();
        Tree tree1 = (Tree) plants.get(0);
        Maple tree2 = (Maple) plants.get(1);
        add(toRemove, tree1);
        add(toRemove, tree2);

        System.out.println("Before remove\n" + plants);

        removeAll(plants, toRemove);

        System.out.println("To remove\n" + toRemove + "\nAfter remove\n" + plants);

        assertEquals(expectedTarget, plants.get(0));
    }

    @Test
    public void containsAllTestTrue() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());
        plants.add(new Oak());

        List<Tree> contains = new ArrayList<>();
        Tree tree1 = (Tree) plants.get(0);
        Maple tree2 = (Maple) plants.get(1);
        add(contains, tree1);
        add(contains, tree2);

        boolean containsAll = containsAll(plants, contains);
        assertTrue(containsAll);
    }

    @Test
    public void containsAllTestFalse() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());
        plants.add(new Oak());

        List<Tree> contains = new ArrayList<>();
        Tree tree1 = (Tree) plants.get(0);
        Maple tree2 = (Maple) plants.get(1);
        add(contains, tree1);
        add(contains, tree2);
        add(contains, new Oak());

        boolean containsAll = containsAll(plants, contains);
        assertFalse(containsAll);
    }

    @Test
    public void containsAnyTestTrue() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());
        plants.add(new Oak());

        List<Tree> contains = new ArrayList<>();
        Tree tree1 = (Tree) plants.get(0);
        add(contains, tree1);
        add(contains, new Maple());
        add(contains, new Oak());

        boolean containsAny = containsAny(plants, contains);
        assertTrue(containsAny);
    }

    @Test
    public void containsAnyTestFalse() {
        List<Plant> plants = new ArrayList<>();
        plants.add(new Tree());
        plants.add(new Maple());
        plants.add(new Oak());

        List<Tree> contains = new ArrayList<>();
        add(contains, new Maple());
        add(contains, new Oak());

        boolean containsAny = containsAny(plants, contains);
        assertFalse(containsAny);
    }

    @Test
    public void rangeTestComparable() {
        List<Integer> target = Arrays.asList(3, 4, 5, 6);
        List<Integer> list = range(Arrays.asList(8, 1, 3, 5, 6, 4), 3, 6);
        assertThat(list, equalTo(target));
    }

    @Test
    public void rangeTestComparator() {
        Comparator<Plant> comparator = new Comparator<Plant>() {
            @Override
            public int compare(Plant o1, Plant o2) {
                String info1 = o1.getInfo();
                String info2 = o2.getInfo();
                if (info1.compareTo(info2) > 0) return 1;
                if (info1.compareTo(info2) < 0) return -1;
                return 0;
            }
        };

        List<Plant> plants = new ArrayList<>();
        plants.add(new Oak());//0
        plants.add(new Tree());//1
        plants.add(new Oak());//2
        plants.add(new Maple());//3
        plants.add(new Plant());//4
        plants.add(new Oak());//5

        List<Plant> target = new ArrayList<>();
        add(target, plants.get(4));
        add(target, plants.get(1));

        List<Plant> result = range(plants, new Plant(), new Tree(), comparator);
        System.out.println(result.toString());
        assertThat(result, equalTo(target));
    }

    @Getter
    class Plant {
        String info = "1 level";
    }
    @Getter
    class Tree extends Plant {
        String info = "2 level";
    }
    @Getter
    class Oak extends Tree {
        String info = "3 level";
    }
    @Getter
    class Maple extends Tree {
        String info = "3 level";
    }
}