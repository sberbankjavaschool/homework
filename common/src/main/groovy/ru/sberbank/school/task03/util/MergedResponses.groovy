package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse

class MergedResponses {
    def symbols
    def dates
    def amounts
    def directions
    def prices
    def count

    MergedResponses(symbol, FxResponse response) {
        this.symbols = symbol
        this.dates = [response.getDate()] as ArrayList<String>
        this.amounts = [response.getAmount()] as ArrayList<String>
        this.directions = [response.getDirection()] as ArrayList<String>
        this.prices = [response.getPrice()] as ArrayList<String>
        this.count = 1
    }

    void addNewItem(FxResponse response){
        dates.add(response.getDate())
        amounts.add(response.getAmount())
        directions.add(response.getDirection())
        prices.add(response.getPrice())
        count = count + 1
    }
}


