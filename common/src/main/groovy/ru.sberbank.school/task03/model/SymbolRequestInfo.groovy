package ru.sberbank.school.task03.model

import ru.sberbank.school.task02.util.FxResponse

class SymbolRequestInfo {
    String symbol

    List<VolumeRequestInfo> volumes

    String max
    String medium
    String min
    FxResponse profitBuy
    FxResponse unprofitBuy
    FxResponse profitSell
    FxResponse unprofitSell
}
