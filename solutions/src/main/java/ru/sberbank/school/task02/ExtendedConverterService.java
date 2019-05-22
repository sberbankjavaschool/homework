package ru.sberbank.school.task02;

import java.math.BigDecimal;

import java.util.Optional;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

public class ExtendedConverterService extends Converter implements ExtendedFxConversionService {
    public ExtendedConverterService(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation,
                                                Symbol symbol,
                                                BigDecimal amount,
                                                Beneficiary beneficiary) {
        throw new UnsupportedOperationException();
    }
}
