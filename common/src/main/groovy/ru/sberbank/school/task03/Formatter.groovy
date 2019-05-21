package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class Formatter implements ResponseFormatter {
    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "Empty list")
        def data = convertResponsesToMap(responses)

        def result = $/
=============================================================
Отчет об изменении котировок для валют ${data.currencies}
=============================================================
/$
        data.symbols.each { item ->
            result += $/
-------------------------------------------------------------
Данные по инструменту: ${item.name}
-------------------------------------------------------------
/$
            item.quotes.each {
                result += """| ${it.date} | ${it.amount} | ${it.direction} | ${it.price}\n"""
            }
            result += $/
В среднем: ${item.evrg}
Min: ${item.min}
Max: ${item.max}

Самая выгодная цена для клиента:
SELL: ${item.sellMax.getPrice()} на объеме ${item.sellMax.getAmount()} | BUY: ${item.buyMin.getPrice()} на объеме ${item.buyMin.getAmount()}

Самая НЕвыгодня цена для клиента:
SELL: ${item.sellMin.getPrice()} на объеме ${item.sellMin.getAmount()} | BUY: ${item.buyMax.getPrice()} на объеме ${item.buyMax.getAmount()}
/$

         }
        result += $/
=============================================================
Всего запросов сделано: ${data.dataCount}
Больше всего запросов по: ${data.maxSymbol.symbol} (${data.maxSymbol.count})
/$
        result
    }

    static Map convertResponsesToMap(List<FxResponse> responses) {
        def symbolsList = []
        def currenciesList = []

        for (response in responses) {
            if (!symbolsList.contains(response.getSymbol())) {
                symbolsList.add(response.getSymbol())
                def currentCurrencies = response.getSymbol().split("/")
                if (currentCurrencies.length == 2) {
                    if (!currenciesList.contains(currentCurrencies[0])) {
                        currenciesList.add(currentCurrencies[0])
                    }
                    if (!currenciesList.contains(currentCurrencies[1])) {
                        currenciesList.add(currentCurrencies[1])
                    }
                }
            }
        }

        def maxSymbol = ['symbol':"", 'count':0]
        def symbolsData = []
        symbolsList.each { symbol ->
            def item = ['name': symbol]
            def responsesSet = responses.findAll{it.getSymbol() == symbol}
            if (responsesSet.size() > maxSymbol.count) {
                maxSymbol.count = responsesSet.size()
                maxSymbol.symbol = symbol
            }

            def quoteList = []
            responsesSet.each {
                quoteList.add(['date': it.getDate(), 'amount': it.getAmount(), 'direction': it.getDirection(), 'price': it.getPrice()])
            }
            item.put('quotes', quoteList)

            def min = responsesSet.min{ ((it.getAmount() as BigDecimal) * (it.getPrice() as BigDecimal)) }
            def max = responsesSet.max{ ((it.getAmount() as BigDecimal) * (it.getPrice() as BigDecimal)) }
            item.put('min', ((min.getAmount() as BigDecimal) * (min.getPrice() as BigDecimal)))
            item.put('max', ((max.getAmount() as BigDecimal) * (max.getPrice() as BigDecimal)))
            item.put('evrg', responsesSet.sum{((it.getAmount() as BigDecimal) * (it.getPrice() as BigDecimal))} / responsesSet.size())

            item.put('sellMax', responsesSet.findAll{it.getDirection() == "SELL"}.max{it.getPrice()})
            item.put('buyMin', responsesSet.findAll{it.getDirection() == "BUY"}.min{it.getPrice()})
            item.put('sellMin', responsesSet.findAll{it.getDirection() == "SELL"}.min{it.getPrice()})
            item.put('buyMax', responsesSet.findAll{it.getDirection() == "BUY"}.max{it.getPrice()})

            symbolsData.add(item)
        }

        def result = [:]
        result.put('currencies', currenciesList)
        result.put('symbols', symbolsData)
        result.put('dataCount', responses.size())
        result.put('maxSymbol', maxSymbol)
        result
    }
}
