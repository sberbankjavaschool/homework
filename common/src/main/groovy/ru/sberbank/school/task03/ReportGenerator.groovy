package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class ReportGenerator {

    Report makeReport(List<FxResponse> responses) {
        Report report = new Report()

        responses.each{ response ->
            report.countRequests++
            if (report.countMostRequests == 0) {
                report.countMostRequests = 1
                report.nameMostRequests = response.symbol
            }
            if (!response.notFound) {
                report.symbols.addAll(response.symbol.split("/"))

                if (report.symbolReport.containsKey(response.symbol)) {
                    def current = report.symbolReport[response.symbol]
                    BigDecimal amount = new BigDecimal(response.amount)
                    BigDecimal price = new BigDecimal(response.price)

                    current.count++
                    if (current.count > report.countMostRequests) {
                        report.countMostRequests = current.count
                        report.nameMostRequests = response.symbol
                    }
                    report.symbolResponses[response.symbol] << response
                    current.min = (current.min > price) ? price : current.min
                    current.max = (current.max < price) ? price : current.max

                    current.mid = chooseMiddleValue(current.min, current.max, current.mid, price)

                    setPrices(current, amount, price, response.direction)

                } else {
                    BigDecimal price = new BigDecimal(response.price)
                    BigDecimal amount = new BigDecimal(response.amount)
                    BigDecimal buyPrice = response.direction.equalsIgnoreCase("BUY") ? price : BigDecimal.ZERO
                    BigDecimal buyVol = response.direction.equalsIgnoreCase("BUY") ? amount : BigDecimal.ZERO
                    BigDecimal sellPrice = response.direction.equalsIgnoreCase("SELL") ? price : BigDecimal.ZERO
                    BigDecimal sellVol = response.direction.equalsIgnoreCase("SELL") ? amount : BigDecimal.ZERO

                    report.symbolReport[response.symbol] = [min  : price, mid: BigDecimal.ZERO, max: price,
                                                            buy  : [goodPrice: buyPrice, goodVol: buyVol,
                                                     badPrice: buyPrice, badVol: buyVol],
                                                            sell : [goodPrice: sellPrice, goodVol: sellVol,
                                                      badPrice: sellPrice, badVol: sellVol],
                                                            count: 1]
                    report.symbolResponses[response.symbol] = [response]
                }
            }
        }

       report
    }

    private BigDecimal chooseMiddleValue(BigDecimal min, BigDecimal max, BigDecimal a, BigDecimal b) {
        def avg = (max - min) / BigDecimal.valueOf(2)
        avg = min + avg
        Math.abs(avg - a) < Math.abs(avg - b) ? a : b
    }

    private void setPrices(def current, BigDecimal amount, BigDecimal price, String operation) {
        boolean goodMore
        boolean badLess

        if (operation.equalsIgnoreCase("SELL")) {
            goodMore = (current.sell.goodPrice != BigDecimal.ZERO) ? current.sell.goodPrice > price : false
            badLess = (current.sell.badPrice != BigDecimal.ZERO) ? current.sell.badPrice < price : false

            current.sell.goodVol = goodMore ? current.sell.goodVol : amount
            current.sell.goodPrice = goodMore ? current.sell.goodPrice : price
            current.sell.badVol = badLess ? current.sell.badVol : amount
            current.sell.badPrice = badLess ? current.sell.badPrice : price

        } else if (operation.equalsIgnoreCase("BUY")) {
            goodMore = (current.buy.goodPrice != BigDecimal.ZERO) ? current.buy.goodPrice > price : true
            badLess = (current.buy.badPrice != BigDecimal.ZERO) ? current.buy.badPrice < price : true

            current.buy.goodVol = goodMore ? amount : current.buy.goodVol
            current.buy.goodPrice = goodMore ? price : current.buy.goodPrice
            current.buy.badVol = badLess ? amount : current.buy.badVol
            current.buy.badPrice = badLess ? price : current.buy.badPrice
        }
    }

}
