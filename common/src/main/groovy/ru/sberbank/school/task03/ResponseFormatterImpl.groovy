package ru.sberbank.school.task03


import ru.sberbank.school.task02.util.ClientOperation
import ru.sberbank.school.task02.util.FxResponse

class ResponseFormatterImpl implements ResponseFormatter {

    BigDecimal maxPrice
    BigDecimal minPrice
    BigDecimal averagePrice
    BigDecimal profitAmount
    BigDecimal unprofitAmount
    int countForSum
    String maxCountName
    int maxCount
    int allCount

    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "передан null")

        String result = "${header(responses)}\n"
        println (result)
        result
    }

    private String header(List<FxResponse> responses) {
        Map<String, List<FxResponse>> map = responses.groupBy { it.symbol }
        def gString = """Отчет об изменении котировок для валют ${map.keySet().join(", ")}\n\n"""
        map.each{ k, v -> gString += getInstruments(k, v) }
        gString += endInfo(map)
        gString
    }

    private String getInstruments(String instruments, List<FxResponse> responses) {
        def gString = """Данные по инструменту: ${instruments}\n"""
        gString += "| Дата и время запроса | amount | operation(direction) | price\n"
        for(FxResponse response : responses) {
            gString += "| " + response.date + " | " + response.amount + " | " + response.direction +
                    " | " + response.price + "\n"
        }
        gString += getAdditionalInfo(responses) + "\n"
        gString
    }

    private String getAdditionalInfo(List<FxResponse> responses) {
        def (responsesBuy, responsesSell) = responses.split {
            it.direction.equals(ClientOperation.BUY.toString())}
        def string = "${buyInfo(responsesBuy)}${sellInfo(responsesSell)}"
        string
    }

    private String buyInfo(List<FxResponse> responsesBuy) {
        def gString = """"""
        if (responsesBuy.size() > 0) {
            gString += "Отчет по операциям покупки:\n${report(responsesBuy)}"
        }
        gString
    }

    private String sellInfo(List<FxResponse> responsesSell) {
        def gString = """"""
        if (responsesSell.size() > 0) {
            gString = "Отчет по операциям продажи:\n${report(responsesSell)}"
        }
        gString
    }

    private String report(List<FxResponse> responses) {
        getValue(responses)
        def gString = """Максимальная цена: ${maxPrice}\nМинимальная цена: ${minPrice}\n"""
        gString += "Cредняя цена: ${averagePrice}\n" +
                "Самая выгодная для клиента цена ${maxPrice} на объеме ${profitAmount}\n" +
                "Самая невыгодная для клиента цена ${minPrice} на объеме ${unprofitAmount}\n"
        gString
    }

    private void getValue(List<FxResponse> responses) {
        def init = true
        for (FxResponse response : responses) {
            BigDecimal tempPrice = new BigDecimal(response.price)
            BigDecimal tempAmount = new BigDecimal(response.amount)
            if(init) {
                maxPrice = tempPrice
                minPrice = tempPrice
                averagePrice = tempPrice
                profitAmount = tempAmount
                unprofitAmount = tempAmount
                init = false
                countForSum = 1
                continue
            }
            if(maxPrice < tempPrice) {
                maxPrice = tempPrice
                profitAmount = tempAmount
            }
            if(minPrice > tempPrice) {
                minPrice = tempPrice
                unprofitAmount = tempAmount
            }
            averagePrice += tempPrice
            countForSum++
        }
        averagePrice = averagePrice / countForSum
    }

    private String endInfo(Map<String, List<FxResponse>> map) {
        getEndValue(map)
        def string = "Всего запросов сделано: ${allCount} \n" +
                "Больше всего запросов по: ${maxCountName} (${maxCount})"
        string
    }

    private void getEndValue(Map<String, List<FxResponse>> map){
        maxCountName
        maxCount
        allCount
        def init = true
        for(String key : map.keySet()) {
            int tempSize = map.get(key).size()
            if(init) {
                maxCountName = key
                maxCount = tempSize
                allCount = tempSize
                init = false
                continue
            }
            if(maxCount < tempSize) {
                maxCountName = key
                maxCount = tempSize
            }
            allCount += tempSize
        }
    }
}