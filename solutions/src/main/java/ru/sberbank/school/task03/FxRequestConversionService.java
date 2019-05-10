package ru.sberbank.school.task03;

import ru.sberbank.school.task02.util.Beneficiary;
import ru.sberbank.school.task02.util.ClientOperation;
import ru.sberbank.school.task02.util.Symbol;

public interface FxRequestConversionService {

    public Symbol getSymbol (String symbol);

    public ClientOperation getClientOperation (String operation);

    public Beneficiary getBeneficiary ();

}
