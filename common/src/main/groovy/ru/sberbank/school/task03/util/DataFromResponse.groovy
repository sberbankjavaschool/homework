package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse

class DataFromResponse {
    Map<String, MergedResponses> responsesMap

    String symbols
    String mostPopular
    String mostPopularCount
    int count

    DataFromResponse(List<FxResponse> responses) {
        responsesMap = createMap(responses)
        symbols = responsesMap.keySet().join(", ")

        Pair<String, Integer> popular = getPopular()
        mostPopular = popular.getKey()
        mostPopularCount = popular.getValue()

        count = responses.size()
    }

    private Map<String, MergedResponses> createMap(List<FxResponse> responses) {
        Map map = [:] as LinkedHashMap<String, MergedResponses>

        responses.each { FxResponse response ->
            def symbol = response.getSymbol()

            if (!map.containsKey(symbol)) {
                map.put(symbol, new MergedResponses(symbol, response))
            } else {
                MergedResponses mr = map[symbol]
                mr.addNewItem(response)
                map[symbol] = mr
            }
        }

        map
    }

    private Pair<String, Integer> getPopular() {
        String symbol
        int currentCount

        for (Map.Entry<String, MergedResponses> entry : responsesMap.entrySet()) {
            if (symbol == null) {
                symbol = entry.getKey()
                currentCount = entry.getValue().getCount()
            } else {
                currentCount > entry.getValue().getCount() ?: entry.getValue().getCount()
            }
        }

        new Pair<>(symbol , currentCount)
    }
}
