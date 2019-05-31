package ru.sberbank.school.task03

class Report {
    LinkedHashMap symbolReport
    LinkedHashMap symbolResponses
    HashSet<String> symbols
    int countRequests
    int countMostRequests
    String nameMostRequests

    Report() {
        symbolReport = new LinkedHashMap()
        symbolResponses = new LinkedHashMap()
        symbols = new HashSet<>()
    }
}
