package ru.sberbank.school.task13;

public class ProperTestTarget {
    private int intValue;
    private boolean booleanVariable;
    private String stringString;

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setBooleanVariable(boolean booleanVariable) {
        this.booleanVariable = booleanVariable;
    }

    public void setStringString(String stringString) {
        this.stringString = stringString;
    }

    @Override
    public String toString() {
        return intValue + " " + booleanVariable + " '" + stringString + "'";
    }
}
