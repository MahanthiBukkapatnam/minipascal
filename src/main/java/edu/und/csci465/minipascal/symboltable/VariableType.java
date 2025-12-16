package edu.und.csci465.minipascal.symboltable;

public enum VariableType {
    FUNCTION,
    PROCEDURE,
    ERROR,
    UNDEFINED,
    QUOTE_STRING,
    INTEGER,
    CHAR,
    BOOLEAN,
    INTEGER_ARRAY,
    CHAR_ARRAY,
    BOOLEAN_ARRAY;

    /**
     * Assignment compatibility. Adjust rules to match your Mini Pascal.
     */
    public static boolean isAssignable(VariableType target, VariableType source) {
        if (target == ERROR || source == ERROR) return true; // avoid cascades
        // strict by default:
        return target == source;
    }

    /**
     * Binary operator typing helper (optional).
     */
    public static VariableType resultOfArithmetic(VariableType a, VariableType b) {
        if (a == ERROR || b == ERROR) return ERROR;
        if (a == INTEGER && b == INTEGER) return INTEGER;
        return ERROR;
    }

    public static VariableType resultOfRelational(VariableType a, VariableType b) {
        if (a == ERROR || b == ERROR) return ERROR;
        // e.g., allow int comparisons with int
        if (a == b && (a == INTEGER || a == CHAR)) return BOOLEAN;
        return ERROR;
    }

    public static VariableType resultOfLogical(VariableType a, VariableType b) {
        if (a == ERROR || b == ERROR) return ERROR;
        if (a == BOOLEAN && b == BOOLEAN) return BOOLEAN;
        return ERROR;
    }

}
