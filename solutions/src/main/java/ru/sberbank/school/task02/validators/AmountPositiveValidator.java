package ru.sberbank.school.task02.validators;

import java.math.BigDecimal;

/**
 * Валидирует положительное значение входящей суммы
 * Если сумма не положительная кидает IllegalArgumentException
 * Created by Gregory Melnikov at 11.05.2019
 */
public class AmountPositiveValidator {

    public static void amountValidate(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
