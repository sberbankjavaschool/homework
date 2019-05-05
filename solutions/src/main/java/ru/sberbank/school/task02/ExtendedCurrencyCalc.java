package ru.sberbank.school.task02;

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
    public Optional<BigDecimal> convertReversed(ClientOperation operation, Symbol symbol,
                                                BigDecimal amount, Beneficiary beneficiary) {
        return convertReversed(operation, symbol, amount, 0, beneficiary);
    }

    @Override
    public Optional<BigDecimal> convertReversed(@NonNull ClientOperation operation, @NonNull Symbol symbol,
                                                @NonNull BigDecimal amount, double delta,
                                                @NonNull Beneficiary beneficiary) {

        if (amount.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException();
        }

        List<Quote> quotes = getExternalQuotesService().getQuotes(symbol);
        List<Quote> suitableQuotes = getSuitableQuotes(quotes, amount, delta);

        if (suitableQuotes == null) {
            return Optional.empty();
        }

        Quote quote = suitableQuotes.get(0);
        for (Quote q : suitableQuotes) {
            if (beneficiary == Beneficiary.BANK && q.getOffer().compareTo(quote.getOffer()) > 0) {
                quote = q;
            } else if (beneficiary == Beneficiary.CLIENT && q.getOffer().compareTo(quote.getOffer()) < 0) {
                quote = q;
            }
        }

        return Optional.of(operation == ClientOperation.BUY ? quote.getOffer() : quote.getBid());
    }

    private List<Quote> getSuitableQuotes(@NonNull List<Quote> quotes, BigDecimal amount, double delta) {
        if (quotes == null || quotes.size() == 0) {
            return null;
        }

        List<Quote> suitableQuotes = new ArrayList<>();
        Quote find = null;

        for (Quote q : quotes) {
            if ((q.getVolumeSize().compareTo(amount) > 0 || q.isInfinity())
                    && (find == null || find.getVolumeSize().compareTo(q.getVolumeSize()) > 0 || find.isInfinity())) {
                find = q;
            }
        }

        if (find == null) {
            for (Quote q : quotes) {
                if (q.getVolumeSize().add(BigDecimal.valueOf(delta)).compareTo(amount) > 0) {
                    suitableQuotes.add(q);
                }
            }

            return suitableQuotes.size() == 0 ? null : suitableQuotes;
        }

        for (Quote q : quotes) {
            if (q.getVolumeSize().equals(find.getVolumeSize())) {
                suitableQuotes.add(q);
            }
        }

        return suitableQuotes;
    }

}
