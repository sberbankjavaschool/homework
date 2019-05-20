package ru.sberbank.school;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchoolTask {
    FX_CALC_02_01("Вал. Калькулятор ч.1", 2),
    FX_CALC_02_02("Вал. Калькулятор ч.2", 3),
    FX_CALC_02_03("Вал. Калькулятор ч.3", 4),
    FX_CALC_03_01("Форматтер", 5),
    GENERICS_06("Generics", 6);

    private final String name;
    private final int code;

}
