package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class ResponseFormatterImpl implements ResponseFormatter {
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */
    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "переданный список операций не инициализирован")

        if (responses.isEmpty()) {
            throw new IllegalArgumentException("Передан пустой список операций")
        }

        String result = "Отчет об изменении котировок для валют "
        Set<String> symbolList = new HashSet<>()
        int mostRequestedSymbolCount = 0
        String mostRequestedSymbol = null

        //Добавляем список валют
        for (FxResponse response : responses) {
            if (symbolList.add(response.getSymbol())) {
                if (symbolList.size() == 1) {
                    result += response.getSymbol()
                } else {
                    result += ", " + response.getSymbol()
                }
            }
        }
        result += "\n"

        for (String symbol : symbolList) {
            result += """
Данные по инструменту: ${symbol}
| Дата и время запроса | amount | operation(direction) | price
"""
            int thisSymbolCount = 0
            int countBuyOperation = 0
            int countSellOperation = 0
            BigDecimal minBuyPrice = 0
            BigDecimal minSellPrice = 0
            BigDecimal maxBuyPrice = 0
            BigDecimal maxSellPrice = 0
            BigDecimal sumBuyPrice = 0
            BigDecimal sumSellPrice = 0
            BigDecimal bestBuyPriceAmount = 0
            BigDecimal bestSellPriceAmount = 0
            BigDecimal worstBuyPriceAmount = 0
            BigDecimal worstSellPriceAmount = 0
            for (FxResponse response : responses) {
                if (response.getSymbol() == symbol) {
                    String date = response.getDate()
                    BigDecimal amount = new BigDecimal(response.getAmount())
                    String direction = response.getDirection()
                    BigDecimal price = new BigDecimal(response.getPrice())
                    result += "| ${date} | ${amount} | ${direction} | ${price}\n"

                    if (response.getDirection() == "BUY") {
                        if (sumBuyPrice == 0) {
                            minBuyPrice = price
                            maxBuyPrice = price
                            bestBuyPriceAmount = worstBuyPriceAmount = amount
                        }
                        sumBuyPrice += price
                        countBuyOperation++
                        if (price < minBuyPrice) {
                            bestBuyPriceAmount = amount
                            minBuyPrice = price
                        } else if (price > maxBuyPrice) {
                            worstBuyPriceAmount = amount
                            maxBuyPrice = price
                        }
                    } else { //(response.getDirection() == "SELL")
                        if (sumSellPrice == 0) {
                            minSellPrice = price
                            maxSellPrice = price
                            bestSellPriceAmount = worstSellPriceAmount = amount
                        }
                        sumSellPrice += price
                        countSellOperation++
                        if (price < minSellPrice) {
                            bestSellPriceAmount = amount
                            minSellPrice = price
                        } else if (price > maxSellPrice) {
                            worstSellPriceAmount = amount
                            maxSellPrice = price
                        }
                    }
                    if (++thisSymbolCount > mostRequestedSymbolCount) {
                        mostRequestedSymbolCount = thisSymbolCount
                        mostRequestedSymbol = symbol
                    }
                }
            }

            result += countBuyOperation != 0 ?
                    """Отчет по операциям покупки:
    минимальная цена: ${minBuyPrice}
    максимальная цена: ${maxBuyPrice}
    средняя цена: ${sumBuyPrice}
    самая выгодная для клиента цена ${minBuyPrice} на объеме ${bestBuyPriceAmount}
    самая невыгодная для клиента цена ${maxBuyPrice} на объеме ${worstBuyPriceAmount} 
"""
                    : """Операций покупки не было
"""

            result += countSellOperation != 0 ?
                    """Отчет по операциям продажи:
    минимальная цена: ${minSellPrice}
    максимальная цена: ${maxSellPrice}
    средняя цена: ${sumSellPrice}
    самая выгодная для клиента цена ${minSellPrice} на объеме ${bestSellPriceAmount}
    самая невыгодная для клиента цена ${maxSellPrice} на объеме ${worstSellPriceAmount}
"""
                    : """Операций продажи не было
"""
        }

        result += """
Всего запросов сделано: ${responses.size()}
Больше всего запросов по: ${mostRequestedSymbol} (${mostRequestedSymbolCount})"""
        return result
    }
}

