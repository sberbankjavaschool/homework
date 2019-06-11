package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

/**
 * 06.06.2019
 * Реализация пула потоков. Количество потоков задается в конструкторе и не меняется.
 *
 * @author Gregory Melnikov
 */

@Solution(10)
public class FixedThreadPool extends ScalableThreadPool {
    public FixedThreadPool(@NonNull int maxPoolSize) {
        super(maxPoolSize, maxPoolSize);
    }
}