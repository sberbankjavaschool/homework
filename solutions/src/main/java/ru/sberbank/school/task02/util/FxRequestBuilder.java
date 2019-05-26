package ru.sberbank.school.task02.util;

import ru.sberbank.school.task02.exeption.BuildRequestsNeverCallException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Создание FxRequest из массива строк
 * Created by Gregory Melnikov at 03.05.2019
 */
public class FxRequestBuilder {

    private static final String SYMBOL = "symbol";
    private static final String DIRECTION = "direction";
    private static final String AMOUNT = "amount";

    private static final List<String> requestArgsList = Arrays.asList(SYMBOL, DIRECTION, AMOUNT);

    /**
     * 03.05.2019
     * Преобразует массив строк в список объектов типа FxRequest
     *
     * @param args - массив строк, напр. аргументы командной строки
     * @return - список объектов FxRequest
     */
    public List<FxRequest> buildRequests(String[] args) {

        List<FxRequest> requests = new ArrayList();

        Iterator<String> iterator = requestArgsList.iterator();

        String[] requestArgs = new String[requestArgsList.size()];

        for (String arg : args) {
            if (!iterator.hasNext()) {
                iterator = requestArgsList.listIterator(0);
            }
            switch (iterator.next()) {
                case SYMBOL:
                    requestArgs[0] = arg;
                    break;
                case DIRECTION:
                    requestArgs[1] = arg;
                    break;
                case AMOUNT:
                    requestArgs[2] = arg;
                    FxRequest request = new FxRequest(
                            requestArgs[0],
                            requestArgs[1],
                            requestArgs[2]);
                    requests.add(request);
                    break;
                default:
                    throw new BuildRequestsNeverCallException("Problem in buildRequests method of FxRequestBuilder");
            }
        }
        return requests;
    }
}