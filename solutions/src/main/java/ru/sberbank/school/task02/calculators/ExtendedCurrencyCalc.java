package ru.sberbank.school.task02.calculators;

import lombok.NonNull;
import ru.sberbank.school.task02.ExtendedFxConversionService;
import ru.sberbank.school.task02.ExternalQuotesService;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExtendedCurrencyCalc extends CurrencyCalc implements ExtendedFxConversionService {

    public ExtendedCurrencyCalc(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, Beneficiary beneficiary) {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount, double delta,
                                                @NonNull Beneficiary beneficiary) {
        List<Quote> quotes = getExternalQuotesService().getQuotes(symbol);
        sortQuotes(quotes);
        List<Quote> suitableQuotes = new ArrayList<>();
        BigDecimal val;
        BigDecimal result;
        BigDecimal previousVolume = null;
        Quote find = null;

        for (Quote q : quotes) {
            val = (operation == ClientOperation.BUY) ? q.getOffer() : q.getBid();
            result = amount.divide(val, RoundingMode.HALF_UP);
            if (previousVolume != null && result.compareTo(previousVolume) < 0) {
                break;
            } else if (q.isInfinity() || result.compareTo(q.getVolumeSize()) < 0) {
                find = q;
                break;
            } else if (result.compareTo(q.getVolumeSize().add(BigDecimal.valueOf(delta))) < 0) {
                suitableQuotes.add(q);
            }
            previousVolume = q.getVolumeSize();
        }

        if (find == null && suitableQuotes.size() == 0) {
            return Optional.empty();
        } else if (suitableQuotes.size() != 0) {
            find = suitableQuotes.get(0);
            boolean compareResult;

            for (Quote q : suitableQuotes) {
                compareResult = (operation == ClientOperation.BUY) ? q.getOffer().compareTo(find.getOffer()) > 0 :
                        q.getBid().compareTo(find.getBid()) < 0;
                if (beneficiary == Beneficiary.BANK && compareResult) {
                    find = q;
                } else if (beneficiary == Beneficiary.CLIENT && compareResult) {
                    find = q;
                }
            }
        }

        return Optional.of(operation == ClientOperation.BUY ? find.getOffer() : find.getBid());
    }

}
