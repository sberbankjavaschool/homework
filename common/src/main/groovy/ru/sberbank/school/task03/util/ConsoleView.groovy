package ru.sberbank.school.task03.util

class ConsoleView {
    ReportDocument document;

    ConsoleView(ReportDocument document) {
        this.document = document;
    }



    void print() {
        println " Отчет об изменении котировок для валютных пар: ${document.getSymbols()} \n";

        document.getSymbolGroups().each { group ->
            println group.toString();
        }

        println "Всего запросов сделано: ${document.getCountRequests()} \n" +
                "Больше всего запросов по: ${document.getBiggerGroup().getSymbol()}";
    }

    @Override
    String toString() {
        GString result = " Отчет об изменении котировок для валютных пар: ${document.getSymbols()} \n";

        document.getSymbolGroups().each { group ->
            result += group.toString();
        }

        result += "Всего запросов сделано: ${document.getCountRequests()} \n" +
                "Больше всего запросов по: ${document.getBiggerGroup().getSymbol()}";

        return result;
    }
}
