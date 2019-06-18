package ru.sberbank.school.task13;

public interface TestClassInterface {

    int methodWithTwoArgs(int a, int b);

    @CachingPolicy(cacheType = CacheType.FILE, fileNamePrefix = "data", identityBy = {String.class, Integer.class})
    String methodWithAnnotations(String word, int multiplier, boolean checkForGrammar);
}
