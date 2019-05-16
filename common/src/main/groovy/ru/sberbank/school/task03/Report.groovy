package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse


class Report {

    int countRequests
    int maxRequestSize
    String maxRequestSymbol
    Map<String, ResponseGroup> responseGroups

    Report(List<FxResponse> responses) {
        responseGroups = calcMapResponses(responses)
        countRequests = responses.size()

        for (ResponseGroup r in responseGroups.values()) {

            if (maxRequestSize < r.getSize()) {
                maxRequestSize = r.getSize()
                maxRequestSymbol = r.getSymbol()
            }
        }
    }

    private Map<String, ResponseGroup> calcMapResponses(List<FxResponse> responses) {

        Map<String, ResponseGroup> symbols = new HashMap<>()

        for (FxResponse r in responses) {
            String symbol = r.getSymbol()

            if (!symbols.containsKey(symbol)) {
                symbols.put(symbol, new ResponseGroup(responses, symbol))
            }
        }
        symbols

    }

    Set<String> getSymbols() {
        responseGroups.keySet()
    }

}
