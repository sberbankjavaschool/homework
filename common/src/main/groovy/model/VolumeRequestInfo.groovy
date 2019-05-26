package model

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
}
