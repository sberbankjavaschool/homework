package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse


class Report {

    int countRequests
    int maxCountRequests
    String symbolRequest
    Map<String, ResponseGroup> responseGroups

    Report(List<FxResponse> responses) {
        responseGroups = calcMapResponses(responses)
        countRequests = responses.size()

        for (ResponseGroup r in responseGroups.values()) {

            if (maxCountRequests < r.getCountRequest()) {
                maxCountRequests = r.getCountRequest()
                symbolRequest = r.getSymbol()
            }
        }
    }

    private Map<String, ResponseGroup> calcMapResponses(List<FxResponse> responses) {

        Map<String, List<FxResponse>> symbols = new HashMap<>()

        for (FxResponse r in responses) {
            String key = r.getSymbol()

            if (symbols.containsKey(key)) {
                symbols.get(key).add(r)
            } else {
                List<FxResponse> value =  new ArrayList<FxResponse>()
                value.add(r)
                symbols.put(key, value)
            }
        }

        Map<String, ResponseGroup> responseGroups = new HashMap<>()

        for (Map.Entry<String, List<FxResponse>> entry in symbols) {
            responseGroups.put(entry.getKey(), new ResponseGroup(entry.getValue(), entry.getKey()))
        }

        responseGroups

    }

    Set<String> getSymbols() {
        responseGroups.keySet()
    }

}
