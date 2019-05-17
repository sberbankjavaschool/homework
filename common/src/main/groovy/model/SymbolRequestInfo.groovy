package model

import ru.sberbank.school.task02.util.FxResponse

class SymbolRequestInfo {
    String symbol
    String min
    String average
    String max
    FxResponse profitBuy
    FxResponse profitSell
    FxResponse lossBuy
    FxResponse lossSell
    List<VolumeRequestInfo> volumes
}
