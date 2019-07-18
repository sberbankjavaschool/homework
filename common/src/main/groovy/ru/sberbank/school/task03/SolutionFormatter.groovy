package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class SolutionFormatter {

    private static BigDecimal maxPrice
    private static BigDecimal minPrice
    private static BigDecimal averagePrice
    private static BigDecimal profitAmount
    private static BigDecimal unprofitAmount
    private static String maxCountName
    private static int maxCount
    private static int allCount

    static String getMaxPrice() {
        return maxPrice.toString()
    }

    static String getMinPrice() {
        return minPrice.toString()
    }

    static String getAveragePrice() {
        return averagePrice.toString()
    }

    static String getProfitAmount() {
        return profitAmount.toString()
    }

    static String getUnprofitAmount() {
        return unprofitAmount.toString()
    }

    static String getMaxCountName() {
        return maxCountName
    }

    static int getMaxCount() {
        return maxCount
    }

    static int getAllCount() {
        return allCount
    }

    static void calculateBodyValue(List<FxResponse> responses) {
        int countForSum
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

    static void calculateEndValue(Map<String, List<FxResponse>> map){
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