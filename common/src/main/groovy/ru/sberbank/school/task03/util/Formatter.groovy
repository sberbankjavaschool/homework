package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter

class Formatter implements ResponseFormatter {

    @Override
    String format(List<FxResponse> responses) {
        List<SymbolGroup> groups = fetchGroups(responses)
        ReportDocument document = new ReportDocument(groups)
        ConsoleView view = new ConsoleView(document)
        return view.toString()
    }


    List<SymbolGroup> fetchGroups(List<FxResponse> responses){
        HashMap<String, SymbolGroup> responsesMap = new HashMap<>()
        responses.each {response ->
            String symbol = response.getSymbol()
            if (responsesMap.containsKey(symbol)) {
                responsesMap.get(symbol).add(response)
            } else {
                SymbolGroup tmp = new SymbolGroup(symbol)
                tmp.add(response)
                responsesMap.put(symbol, tmp)
            }
        }
        new ArrayList<SymbolGroup>(responsesMap.values())
    }
}
