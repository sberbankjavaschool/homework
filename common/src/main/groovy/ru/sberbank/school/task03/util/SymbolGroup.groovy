package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.ClientOperation
import ru.sberbank.school.task02.util.FxResponse

import java.math.RoundingMode

class SymbolGroup {
    String symbol;
    List<FxResponse> responses;
    HashMap<String, Volume> volumes;

    BigDecimal minPriceBuy = BigDecimal.ZERO;
    BigDecimal maxPriceBuy = BigDecimal.ZERO;
    BigDecimal avgPriceBuy = BigDecimal.ZERO;

    BigDecimal minPriceSell = BigDecimal.ZERO;
    BigDecimal maxPriceSell = BigDecimal.ZERO;
    BigDecimal avgPriceSell = BigDecimal.ZERO;

    BigDecimal sumPriceBuy = BigDecimal.ZERO;
    BigDecimal sumPriceSell = BigDecimal.ZERO;

    int countRespSell = 0;
    int countRespBuy = 0;


    SymbolGroup(String symbol) {
        this.symbol = symbol;
        this.responses = new ArrayList<>();
        this.volumes = new HashMap<>();
    }

    void add(FxResponse response){
        if (!response.isNotFound()) {
            this.responses.add(response);
            updateCounters(response);
            calculateVolumes(response);
            updateMinMax(response);
            updateAvg(response);
        }
    }

    int size() {
        responses.size();
    }

    private void updateCounters(FxResponse response) {
        ClientOperation.BUY.equals(ClientOperation.valueOf(response.getDirection())) ?
            countRespBuy++ : countRespSell++;
    }

    private void calculateVolumes(FxResponse response) {
        String amount = response.getAmount();
        if (volumes.containsKey(amount)) {
            volumes.get(amount).updatePrices(response);
        } else {
            Volume tmp = new Volume(amount);
            tmp.updatePrices(response);
            volumes.put(amount, tmp);
        }
    }

    private void updateAvg(FxResponse response) {
        if (ClientOperation.valueOf(response.getDirection()) == ClientOperation.BUY) {
            sumPriceBuy = sumPriceBuy.plus(new BigDecimal(response.getPrice()));
            avgPriceBuy = sumPriceBuy.divide(new BigDecimal(countRespBuy),2,RoundingMode.HALF_UP);
        } else {
            sumPriceSell = sumPriceSell.plus(new BigDecimal(response.getPrice()));
            avgPriceSell = sumPriceSell.divide(new BigDecimal(countRespSell), 2, RoundingMode.HALF_UP);
        }
    }

    private void updateMinMax(FxResponse response) {
        BigDecimal price = new BigDecimal(response.getPrice());
        if (ClientOperation.valueOf(response.getDirection()) == ClientOperation.BUY) {
            if (maxPriceBuy.compareTo(BigDecimal.ZERO) <= 0) {
                maxPriceBuy = price;
            }

            if (minPriceBuy.compareTo(BigDecimal.ZERO) <= 0) {
                minPriceBuy = price;
            }

            minPriceBuy = price < minPriceBuy ? price : minPriceBuy;
            maxPriceBuy = price > maxPriceBuy ? price : maxPriceBuy;
        } else {
            if (maxPriceSell.compareTo(BigDecimal.ZERO) <= 0) {
                maxPriceSell = price;
            }

            if (minPriceSell.compareTo(BigDecimal.ZERO) <= 0) {
                minPriceSell = price;
            }

            minPriceSell = price < minPriceSell ? price : minPriceSell;
            maxPriceSell = price > maxPriceSell ? price : maxPriceSell;
        }
    }

    @Override
    String toString() {
        GString result = "Данные по инструменту ${this.getSymbol()}: \n";
        this.responses.each { response ->
            result += "${response.getDate()} \t ${response.getAmount()} \t ${response.getDirection()} \t ${response.getPrice()} \n";
        }
        result += "Данные по суммам [мин/сред/макс]: \n Продажа: ${minPriceSell} \t ${avgPriceSell} \t ${maxPriceSell} \n " +
                     "Покупка: ${minPriceBuy} \t ${avgPriceBuy} \t ${maxPriceBuy} \n";
        this.volumes.values().each { volume ->
            result += volume.toString();
        }

        return result.toString();
    }
}
