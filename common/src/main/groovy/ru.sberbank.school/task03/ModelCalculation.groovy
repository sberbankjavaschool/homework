package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.model.DocumentInfo
import ru.sberbank.school.task03.model.SymbolRequestInfo
import ru.sberbank.school.task03.model.VolumeRequestInfo

class ModelCalculation {
    List<FxResponse> responses

    ModelCalculation(List<FxResponse> responses) {
        this.responses = responses
    }

    DocumentInfo calculation() {
        DocumentInfo documentInfo = new DocumentInfo()

        Map<String, List<FxResponse>> symbolsMap = getMapSymbol(responses)

        documentInfo.headingSymbols = symbolsMap.keySet().join(" ,")

        List<SymbolRequestInfo> symbolRequests = new ArrayList<>()

        for (Map.Entry<String, List<FxResponse>> element in symbolsMap) {
            SymbolRequestInfo symbolRequest = new SymbolRequestInfo()

            symbolRequest.symbol = element.key

            List<VolumeRequestInfo> volumes = new ArrayList<>()

            for (FxResponse response : element.value) {
                volumes.add(new VolumeRequestInfo(response.date, response.amount, response.direction, response.price))
            }

            symbolRequest.volumes = volumes

            symbolRequest.max = max(element.value)
            symbolRequest.medium = medium(element.value)
            symbolRequest.min = min(element.value)

            choiceResponse(symbolRequest, element.value)

            symbolRequests.add(symbolRequest)
        }

        documentInfo.symbolRequest = symbolRequests

        def maxSymbolRequest = maxCountResponse(symbolsMap)

        documentInfo.numberRequest = responses.size()
        documentInfo.maxSymbol = maxSymbolRequest.key
        documentInfo.maxNumberSymbolRequest = maxSymbolRequest.value.size()

        documentInfo
    }

    Map<String, List<FxResponse>> getMapSymbol(List<FxResponse> responses) {
        Map<String, List<FxResponse>> symbols = new HashMap<>()

        for (FxResponse response in responses) {
            String symbol = response.symbol

            if (!symbols.containsKey(symbol)) {
                List<FxResponse> symbolResponse = new ArrayList<>()
                symbolResponse.add(response)
                symbols.put(symbol, symbolResponse)
            } else {
                symbols.get(symbol).add(response)
            }
        }
        symbols
    }

    String max(List<FxResponse> responses) {
        String result = responses.get(0).amount
        for (FxResponse response : responses) {
            if (new BigDecimal(response.amount) <=> new BigDecimal(result) == 1) {
                result = response.amount
            }
        }
        result
    }

    String min(List<FxResponse> responses) {
        String result = responses.get(0).amount
        for (FxResponse response : responses) {
            if (new BigDecimal(response.amount) <=> new BigDecimal(result) == -1) {
                result = response.amount
            }
        }
        result
    }

    String medium(List<FxResponse> responses) {
        BigDecimal sum = BigDecimal.ZERO

        for (FxResponse response : responses) {
            sum = sum.add(new BigDecimal(response.amount))
        }

        sum.divide(BigDecimal.valueOf(responses.size()), 2, BigDecimal.ROUND_HALF_UP).toString()
    }


    void choiceResponse (SymbolRequestInfo symbolRequest, List<FxResponse> responses) {
        FxResponse profitBuy = null
        FxResponse unprofitBuy = null
        FxResponse profitSell = null
        FxResponse unprofitSell = null

        for (FxResponse response in responses) {
            if(response.direction == "BUY") {
                if(profitBuy == null && unprofitBuy == null) {
                    profitBuy = response
                    unprofitBuy = response
                }else {
                    if (new BigDecimal(response.price) <=> new BigDecimal(profitBuy.price) == -1) {
                        profitBuy = response
                    }
                    if (new BigDecimal(response.price) <=> new BigDecimal(unprofitBuy.price) == 1) {
                        unprofitBuy = response
                    }
                }
            } else {
                if(profitSell == null && unprofitSell == null) {
                    profitSell = response
                    unprofitSell = response
                }else {
                    if (new BigDecimal(response.price) <=> new BigDecimal(profitSell.price) == -1) {
                        profitSell = response
                    }
                    if (new BigDecimal(response.price) <=> new BigDecimal(unprofitSell.price) == 1) {
                        unprofitSell = response
                    }
                }
            }
        }

        symbolRequest.profitBuy = profitBuy
        symbolRequest.unprofitBuy = unprofitBuy
        symbolRequest.profitSell = profitSell
        symbolRequest.unprofitSell = unprofitSell
    }

    Map.Entry<String, List<FxResponse>> maxCountResponse(Map<String, List<FxResponse>> symbolsMap) {
        Map.Entry<String, List<FxResponse>> result
        for (Map.Entry<String, List<FxResponse>> element : symbolsMap) {
            if (result == null) {
                result = element
            } else {
                if (result.value.size() < element.value.size()) {
                    result = element
                }
            }
        }

        result
    }

}
