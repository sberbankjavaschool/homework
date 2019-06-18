package ru.sberbank.school.task13;

public class TestClassForCaching implements TestClassInterface {

    @Override
    public int methodWithTwoArgs(int a, int b) {
        return a + b;
    }

    @Override
    public String methodWithAnnotations(String word, int multiplier, boolean checkForGrammar) {
        if (checkForGrammar) {
            /* will be checked for grammar first */
        }
        for (int i = 0; i < multiplier; i++) {
            word += word;
        }
        return word;
    }

}
