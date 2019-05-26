package ru.sberbank.school.task02.service;

import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExtendedFxConversionServiceImpl extends FxConversionServiceImpl implements ExtendedFxConversionService {


    ExtendedFxConversionServiceImpl(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, Beneficiary beneficiary) {
        this.list = externalQuotesService.getQuotes(symbol);

        List<BigDecimal> checkList = new ArrayList<>();

        for (Quote quote : list) {

            BigDecimal purchasePrice = getPurchasePrice(operation, quote);
            BigDecimal divAmount = amount.divide(purchasePrice, 10, BigDecimal.ROUND_HALF_UP);

            if (quote.getVolumeSize().compareTo(getUpperVolume(divAmount)) == 0) {
                checkList.add(purchasePrice);
            }
        }

        BigDecimal price = getPrice(operation, checkList, beneficiary);

        if (!price.equals(BigDecimal.ZERO)) {
            return Optional.of(BigDecimal.ONE.divide(price, 10, BigDecimal.ROUND_HALF_UP));
        }

        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol, BigDecimal amount, double delta, Beneficiary beneficiary) {
        return Optional.empty();
    }

    private BigDecimal getPrice(ClientOperation operation, List<BigDecimal> checkList, Beneficiary beneficiary) {

        if (!checkList.isEmpty()) {
            return (beneficiary == Beneficiary.CLIENT && operation == ClientOperation.SELL ||
                    beneficiary == Beneficiary.BANK && operation == ClientOperation.BUY)
                    ? Collections.max(checkList) : Collections.min(checkList);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getPurchasePrice(ClientOperation operation, Quote quote) {
        return (operation == ClientOperation.BUY) ? quote.getOffer() : quote.getBid();
    }


}
