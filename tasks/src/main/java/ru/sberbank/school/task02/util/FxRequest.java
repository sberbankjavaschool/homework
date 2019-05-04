package ru.sberbank.school.task02.util;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FxRequest implements Serializable {
    private final String symbol;
    private final String direction;
    private final String amount;
}
