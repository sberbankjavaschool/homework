package ru.sberbank.school.task03.util

class BodyData {
    private def textOfBody
    private def mostPopular
    private def mostPopularCount
    private def count

    BodyData(textOfBody, mostPopular, mostPopularCount, count) {
        this.textOfBody = textOfBody
        this.mostPopular = mostPopular
        this.mostPopularCount = mostPopularCount
        this.count = count
    }
}
