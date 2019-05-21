package ru.sberbank.school.task02.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FxRequest implements Serializable {
    private final String symbol;
    private final String direction;
    private final String amount;
}
