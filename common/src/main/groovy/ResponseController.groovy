import model.DocumentInfo
import model.SymbolRequestInfo
import model.VolumeRequestInfo
import ru.sberbank.school.task02.util.FxResponse

class ResponseController {

    static DocumentInfo getDocument(List<FxResponse> responses){
        DocumentInfo doc = new DocumentInfo()

        List<SymbolRequestInfo> symbolRequestList = []
        Map<String, List<FxResponse>> responseMap = getSymbolMappedResponses(responses)

        StringJoiner headerSymbols = new StringJoiner(", ")
        String  requestCounter = responses.size().toString()
        String maxSymbolRequested = ""
        int maxSymbolRequestedCounter = 0

        for (Map.Entry<String, List<FxResponse>> symbolKey in responseMap) {
            SymbolRequestInfo symbolInfo = new SymbolRequestInfo()
            symbolInfo.symbol = symbolKey.key
            symbolRequestList.add(fillSymbolRequestInfo(symbolInfo, symbolKey.value))

            headerSymbols.add(symbolKey.key)
            if (symbolKey.value.size() > maxSymbolRequestedCounter) {
                maxSymbolRequestedCounter = symbolKey.value.size()
                maxSymbolRequested = symbolKey.key
            }

            doc.headerSymbols = headerSymbols
            doc.requestCounter = requestCounter
            doc.maxSymbolRequested = maxSymbolRequested
            doc.maxSymbolRequestedCounter = maxSymbolRequestedCounter
            doc.symbolRequestInfos = symbolRequestList
        }
        return doc
    }

    static Map<String, List<FxResponse>> getSymbolMappedResponses(List<FxResponse> responses) {
        Map<String, List<FxResponse>> mappedResponses = new HashMap<>()

        for (response in responses) {
            if (!mappedResponses.containsKey(response.symbol)) {
                List<FxResponse> list = []
                list.add(response)
                mappedResponses.put(response.symbol, list)
            } else {
                mappedResponses.get(response.symbol).add(response)
            }
        }
        mappedResponses
    }

    static SymbolRequestInfo fillSymbolRequestInfo(SymbolRequestInfo toFill, List<FxResponse> resp) {
        toFill.volumes = []
        String min = resp.get(0).amount
        String max = resp.get(0).amount
        BigDecimal sum = BigDecimal.ZERO

        for (element in resp) {
            toFill.volumes.add(new VolumeRequestInfo(element.date,
                    element.amount, element.direction, element.price))

            if ((new BigDecimal(element.amount) <=> new BigDecimal(min)) < 0) {
                min = element.amount
            } else if ((new BigDecimal(element.amount) <=> new BigDecimal(max)) > 0){
                max = element.amount
            }
            sum = sum.add(new BigDecimal(element.amount))
        }
        println "${sum}"
        toFill.average = sum.divide(BigDecimal.valueOf(resp.size()), 2, BigDecimal.ROUND_HALF_UP).toString()
        toFill.max = max
        toFill.min = min
        fillSymbolRequestProfitLoss(toFill, resp)
        return toFill
    }

    static SymbolRequestInfo fillSymbolRequestProfitLoss(SymbolRequestInfo toFill, List<FxResponse> resp) {
        List<FxResponse> buy = []
        List<FxResponse> sell = []
        resp.each {it -> it.direction.equalsIgnoreCase("buy") ? buy.add(it) : sell.add(it)}

        FxResponse minBuy = buy.get(0)
        FxResponse maxBuy = buy.get(0)
        FxResponse minSell = sell.get(0)
        FxResponse maxSell = sell.get(0)

        for (element in buy) {
            if ((new BigDecimal(element.price) <=> new BigDecimal(minBuy.price)) < 0) {
                minBuy = element
            }

            if ((new BigDecimal(element.price) <=> new BigDecimal(maxBuy.price)) > 0) {
                maxBuy = element
            }
        }

        for (element in sell) {
            if ((new BigDecimal(element.price) <=> new BigDecimal(minSell.price)) < 0) {
                minSell = element
            }

            if ((new BigDecimal(element.price) <=> new BigDecimal(maxSell.price)) > 0) {
                maxSell = element
            }
        }

        toFill.profitBuy = minBuy
        toFill.lossBuy = minBuy
        toFill.profitSell = maxSell
        toFill.lossSell = minSell
        return toFill
    }
}
