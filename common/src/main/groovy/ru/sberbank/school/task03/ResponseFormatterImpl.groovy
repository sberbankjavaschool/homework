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
        String result = """"""
        result += getHeader(responses)
        result += getBody(responses)
        result += getFooter(responses)
        return result
    }

    String getHeader(List<FxResponse> responses) {
        String result = """Отчет об изменении котировок для валют """
        Set<String> symbolList = new HashSet<>()
        for (FxResponse response : responses) {
            if (symbolList.add(response.getSymbol())) {
                if (symbolList.size() == 1) {
                    result += response.getSymbol()
                } else {
                    result += ", " + response.getSymbol()
                }
            }
        }
        result
    }

    String getFooter(List<FxResponse> responses) {
        int mostRequestedSymbolCount = 0
        String mostRequestedSymbol = null
        Set<String> symbolList = new HashSet<>()
        for (FxResponse response : responses) {
            symbolList.add(response.getSymbol())
        }
        for (String symbol : symbolList) {
            int thisSymbolCount = 0
            for (FxResponse response : responses) {
                if (response.getSymbol() == symbol) {
                    if (++thisSymbolCount > mostRequestedSymbolCount) {
                        mostRequestedSymbolCount = thisSymbolCount
                        mostRequestedSymbol = symbol
                    }
                }
            }
        }
        String result = """\nВсего запросов сделано: ${responses.size()}
Больше всего запросов по: ${mostRequestedSymbol} (${mostRequestedSymbolCount})"""
    }

    String getBody(List<FxResponse> responses) {
        String result = "\n"
        Set<String> symbolList = new HashSet<>()
        for (FxResponse response : responses) {
            symbolList.add(response.getSymbol())
        }
        result += getSymbolsInfo(symbolList, responses)
    }

    String getSymbolsInfo(HashSet<String> symbolList, List<FxResponse> responses) {
        String result = """"""
        for (String symbol : symbolList) {
            InfoAboutOperations info = new InfoAboutOperations()
            result += getSymbolTopic(symbol)
            for (FxResponse response : responses) {
                if (response.getSymbol() == symbol) {
                    String date = response.getDate()
                    BigDecimal amount = new BigDecimal(response.getAmount())
                    String direction = response.getDirection()
                    BigDecimal price = new BigDecimal(response.getPrice())
                    result += "| ${date} | ${amount} | ${direction} | ${price}\n"

                    if (response.getDirection() == "BUY") {
                        if (!info.sumBuyPrice) {
                            info.minBuyPrice = price
                            info.maxBuyPrice = price
                            info.bestBuyPriceAmount = amount
                            info.worstBuyPriceAmount = amount
                        }
                        info.sumBuyPrice += price
                        info.incCountBuyOperation()
                        if (price < info.minBuyPrice) {
                            info.bestBuyPriceAmount = amount
                            info.minBuyPrice = price
                        } else if (price > info.maxBuyPrice) {
                            info.worstBuyPriceAmount = amount
                            info.maxBuyPrice = price
                        }
                    } else { //(response.getDirection() == "SELL")
                        if (!info.sumSellPrice) {
                            info.minSellPrice = price
                            info.maxSellPrice = price
                            info.bestSellPriceAmount = amount
                            info.worstSellPriceAmount = amount
                        }
                        info.sumSellPrice += price
                        info.incCountSellOperation()
                        if (price < info.minSellPrice) {
                            info.bestSellPriceAmount = amount
                            info.minSellPrice = price
                        } else if (price > info.maxSellPrice) {
                            info.worstSellPriceAmount = amount
                            info.maxSellPrice = price
                        }
                    }
                }
            }
            result += info.getBuyReport()
            result += info.getSellReport()
        }
        result
    }

    String getSymbolTopic(String symbol) {
        """\nДанные по инструменту: ${symbol}
| Дата и время запроса | amount | operation(direction) | price\n"""
    }
}

class InfoAboutOperations {
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

    int incCountBuyOperation() {
        countBuyOperation++
    }

    int incCountSellOperation() {
        countSellOperation++
    }

    BigDecimal getMeanBuyPrice() {
        (sumBuyPrice / countBuyOperation).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    BigDecimal getMeanSellPrice() {
        (sumSellPrice / countSellOperation).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    String getSellReport() {
        countSellOperation != 0 ?
                """Отчет по операциям продажи:
    минимальная цена: $minSellPrice
    максимальная цена: $maxSellPrice
    средняя цена: ${getMeanSellPrice()}
    самая выгодная для клиента цена $minSellPrice на объеме $bestSellPriceAmount
    самая невыгодная для клиента цена $maxSellPrice на объеме $worstSellPriceAmount\n"""
                : """Операций продажи не было\n"""
    }

    String getBuyReport() {
        countBuyOperation != 0 ?
                """Отчет по операциям покупки:
    минимальная цена: $minBuyPrice
    максимальная цена: $maxBuyPrice
    средняя цена: ${getMeanBuyPrice()}
    самая выгодная для клиента цена $minBuyPrice на объеме $bestBuyPriceAmount
    самая невыгодная для клиента цена $maxBuyPrice на объеме $worstBuyPriceAmount\n"""
                : """Операций покупки не было\n"""
    }
}