package ru.sberbank.school.task11;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

@Solution(11)
public class ConcurrentFixedThreadPool extends ConcurrentScalableThreadPool {
    public ConcurrentFixedThreadPool(@NonNull int maxPoolSize) {
        super(maxPoolSize, maxPoolSize);
    }
}