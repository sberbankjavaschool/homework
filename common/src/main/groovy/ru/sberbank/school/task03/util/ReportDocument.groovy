package ru.sberbank.school.task03.util

class ReportDocument {
    int countRequests = 0;
    SymbolGroup biggerGroup;
    List<SymbolGroup> symbolGroups;
    StringBuilder symbols = new StringBuilder();

    ReportDocument(List<SymbolGroup> symbolGroups) {
        this.symbolGroups = symbolGroups;
        updateStats();
    }

    private void updateStats() {
        countRequests = 0;
        symbols.setLength(0);
        symbolGroups.each { group ->
            countRequests += group.size();
            symbols.append(group.getSymbol()).append(" ");
            if (biggerGroup == null || biggerGroup.size() < group.size()) {
                biggerGroup = group;
            }
        }
    }
}
