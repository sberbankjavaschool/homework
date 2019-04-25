package ru.sberbank.school.task02;

import java.math.BigDecimal;
import java.util.Optional;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

public interface ExtendedFxConversionService extends FxConversionService {
    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема
     * только в том случае если есть полное совпадение.
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param beneficiary В чью пользу осуществляется округление
     * @return Цена для указанного объема
     */
    Optional<BigDecimal> convertReversed(ClientOperation operation,
                                         Symbol symbol,
                                         BigDecimal amount,
                                         Beneficiary beneficiary);

    /**
     * Возвращает значение цены единицы котируемой валюты для указанного объема.
     *
     * @param operation   вид операции
     * @param symbol      Инструмент
     * @param amount      Объем
     * @param delta       допустимое отклонение при отсутствии точного попадания
     * @param beneficiary В чью пользу осуществляется округление
     * @return Цена для указанного объема
     */
    default Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                 Symbol symbol,
                                                 BigDecimal amount,
                                                 double delta,
                                                 Beneficiary beneficiary) {
        throw new UnsupportedOperationException();
    }
}
