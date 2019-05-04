package ru.sberbank.school.task02.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.sberbank.school.task02.util.Symbol;

/**
 * Класс для работы с валютными парами
 * Created by Gregory Melnikov at 03.05.2019
 */

@Getter
@AllArgsConstructor
public enum Symbols {

    USD_RUB ("USD/RUB", Symbol.USD_RUB, Symbol.USD_RUB.isCross()),
    RUB_USD ("RUB/USD", Symbol.RUB_USD, Symbol.RUB_USD.isCross());

    private final String key;
    private final Symbol symbol;
    private final boolean isCross;
}
