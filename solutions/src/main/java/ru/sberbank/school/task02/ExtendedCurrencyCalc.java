package ru.sberbank.school.task02;

import com.sun.org.apache.xpath.internal.operations.Quo;
import org.eclipse.jgit.annotations.NonNull;
import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Quote;
import ru.sberbank.school.task02.util.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExtendedCurrencyCalc extends CurrencyCalc implements ExtendedFxConversionService {

    public ExtendedCurrencyCalc(ExternalQuotesService externalQuotesService) {
        super(externalQuotesService);
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount, @NonNull Beneficiary beneficiary) {
        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException();
        }

        List<Quote> quotes = getExternalQuotesService().getQuotes(symbol);
        List<Quote> suitableQuotes = getSuitableQuotes(quotes, amount);

        if (suitableQuotes.size() == 0) {
            return Optional.empty();
        }

        for (Quote quote : quotes) {

        }

        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, double delta, Beneficiary beneficiary) {
        return Optional.empty();
    }

    private List<Quote> getSuitableQuotes (List<Quote> quotes, BigDecimal amount) {
        List<Quote> suitableQuotes = new ArrayList<>();
        Quote find = null;

        for (Quote q : quotes) {
            if ((q.getVolumeSize().compareTo(amount) > 0 || BigDecimal.ZERO.compareTo(q.getVolumeSize()) > 0)
                    && (find == null || find.getVolumeSize().compareTo(q.getVolumeSize()) > 0
            || BigDecimal.ZERO.compareTo(find.getVolumeSize()) > 0)) {
                find = q;
            }
        }

        if (find == null) {
            return null;
        }

        for (Quote q : quotes) {
            if (q.getVolumeSize().equals(find.getVolumeSize())) {
                suitableQuotes.add(q);
            }
        }

        return suitableQuotes;
    }
}
