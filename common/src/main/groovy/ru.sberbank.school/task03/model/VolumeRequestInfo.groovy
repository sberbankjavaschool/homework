package ru.sberbank.school.task03.model

import ru.sberbank.school.task02.util.FxResponse

class VolumeRequestInfo {

    String date
    String amount
    String direction
    String price

    VolumeRequestInfo(String date, String amount, String direction, String price) {
        this.date = date
        this.amount = amount
        this.direction = direction
        this.price = price
    }

    VolumeRequestInfo(FxResponse response) {

        this.date = response.getDate()
        this.amount = response.getAmount()
        this.direction = response.getDirection()
        this.price = response.getPrice()
    }
}
