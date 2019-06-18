package ru.sberbank.school.task13.cacheproxy.utils;

import ru.sberbank.school.task13.beanfieldcopier.utils.Player;

public class TestClassImpl implements TestClass {
    private String testField;
    private int countCalls = 0;

    public TestClassImpl(String smth) {
        this.testField = smth;
    }

    @Override
    public String getResFromMemory() {
        countCalls++;
        return testField;
    }

    @Override
    public String getResFromMemoryWithArgs(String smth, int i) {
        countCalls++;
        return testField;
    }

    @Override
    public String getResFromFile() {
        countCalls++;
        return testField;
    }

    @Override
    public String getResFromFileWithArgs(String smth, int i) {
        countCalls++;
        return testField;
    }

    @Override
    public String getResFromFileWithPlayers(Player player1, Player player2) {
        countCalls++;
        return player1.toString() + " " + player2.toString();
    }

    @Override
    public String getResFromZip() {
        countCalls++;
        return testField;
    }

    @Override
    public int getCountCalls() {
        return countCalls;
    }

}
