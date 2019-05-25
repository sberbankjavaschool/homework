package ru.sberbank.school.model

class SymbolRequestInfo {

    String symbol
    String min
    String max
    String average
    String bestSellingPrice
    String bestSellingAmount
    String worstSellingPrice
    String worstSellingAmount
    String bestBuyingPrice
    String bestBuyingAmount
    String worstBuyingPrice
    String worstBuyingAmount
    List<VolumeRequestInfo> volumeRequestInfoList
}
