package ru.sberbank.school.task02.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.sberbank.school.task02.util.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с валютными парами
 * Created by Gregory Melnikov at 03.05.2019
 */

@Getter
@AllArgsConstructor
public enum Symbols {

    USD_RUB("USD/RUB", Symbol.USD_RUB),
    RUB_USD("RUB/USD", Symbol.RUB_USD);

    private final String key;
    private final Symbol symbol;

    private static final Map<String, Symbol> searchMap = new HashMap<>();

    static {
        for (Symbols symbol : Symbols.values()) {
            searchMap.put(symbol.getKey(), symbol.getSymbol());
        }
    }

    public static Symbol get(String key) {
        return searchMap.get(key);
    }
}
