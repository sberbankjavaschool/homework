package ru.sberbank.school.task08;

public abstract class ManagerTest {

    protected static final String PATH =
            "D:\\dev\\java\\Test";
    protected static final String FILENAME = "file.txt";

    abstract void saveEmptyGameObjects();

    abstract void saveAndLoadGame() throws SaveGameException;
}
