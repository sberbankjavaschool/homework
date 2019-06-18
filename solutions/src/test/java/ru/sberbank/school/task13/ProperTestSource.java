package ru.sberbank.school.task13;

public class ProperTestSource {
    private int intValue;
    private boolean booleanVariable;
    private String stringString;

    public ProperTestSource(int intValueSource, boolean booleanVariableSource, String stringStringSource) {
        this.intValue = intValueSource;
        this.booleanVariable = booleanVariableSource;
        this.stringString = stringStringSource;
    }

    public int getIntValue() {
        return intValue;
    }

    public boolean isBooleanVariable() {
        return booleanVariable;
    }

    public String getStringString() {
        return stringString;
    }

}
