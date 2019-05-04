package ru.sberbank.school.task02;

import lombok.NonNull;
import ru.sberbank.school.task02.exception.FxConversionException;
import ru.sberbank.school.task02.exception.WrongSymbolException;
import ru.sberbank.school.task02.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс-реализация клиента для получения расчетных курсов.
 */
public class FxClient implements FxClientController {

    /**
     * Текущий конвертер. Инициализируется через конструктор.
     */
    private FxConversionService currentConverter;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    FxClient(FxConversionService converter) {
        currentConverter = converter;
    }

    /**
     * Функция обрабатывает список запросов и возвращает список ответов.
     *
     * @param requests список запросов
     * @throws IllegalArgumentException если amount не является числом, либо amount <= 0
     *                                  если операция задана неправильно
     * @throws NullPointerException     если лист запросов null
     *                                  если какой-либо из запросов null
     *                                  если какая-либо составляющая запросов null
     * @throws FxConversionException    если лист котировок пуст
     * @throws WrongSymbolException     если symbol не поддерживается
     */
    @Override
    public List<FxResponse> fetchResult(@NonNull List<FxRequest> requests) {
        List<FxResponse> responses = new ArrayList<>();
        for (FxRequest req : requests) {
            responses.add(fetchResult(req));
        }
        return responses;
    }


    /**
     * Функция обрабатывает одиночный запрос и возвращает одиночный ответ.
     *
     * @param requests запрос
     * @throws IllegalArgumentException если amount не является числом, либо amount <= 0
     *                                  если операция задана неправильно
     * @throws NullPointerException     если запрос null
     *                                  если какая-либо составляющая запроса null
     * @throws FxConversionException    если лист котировок пуст
     * @throws WrongSymbolException     если symbol не поддерживается
     */
    @Override
    public FxResponse fetchResult(@NonNull FxRequest requests)
            throws FxConversionException {
        Symbol symbol = getSymbol(requests);
        BigDecimal amount = getAmount(requests);
        ClientOperation operation = getOperation(requests);


        LocalDateTime localDate = LocalDateTime.now();
        String date = localDate.format(formatter);

        BigDecimal price = currentConverter.convert(operation, symbol, amount);

        return new FxResponse(symbol.getSymbol(),
                price.toString(),
                amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                date,
                operation.toString(),
                false);

    }

    private BigDecimal getAmount(FxRequest r) throws IllegalArgumentException {
        try {
            return new BigDecimal(r.getAmount());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Amount is not a number");
        }
    }

    private ClientOperation getOperation(FxRequest r) throws IllegalArgumentException {
        try {
            return ClientOperation.valueOf(r.getDirection().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal operation");
        }
    }

    private Symbol getSymbol(FxRequest r) throws IllegalArgumentException {
        String symbol = r.getSymbol();
        if (symbol.equals(Symbol.RUB_USD.getSymbol())) {
            return Symbol.RUB_USD;
        } else if (symbol.equals(Symbol.USD_RUB.getSymbol())) {
            return Symbol.USD_RUB;
        } else {
            throw new IllegalArgumentException("Illegal symbol");
        }
    }
}
