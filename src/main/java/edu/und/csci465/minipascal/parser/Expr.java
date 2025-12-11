package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.symboltable.VariableType;

import java.util.Objects;

/**
 * Expr = attribute bundle for an expression during parsing / type checking.
 *
 * Inherited attributes (passed down from context):
 *  - expectedType: the type the context wants (e.g., RHS of assignment expects LHS type)
 *  - wantLValue:   whether context requires an assignable location (e.g., left side of :=)
 *
 * Synthesized attributes (computed from the expression itself):
 *  - type:        actual type of the expression
 *  - isLValue:    whether the expression denotes a storage location (var/array element)
 *  - isConst:     whether the expression is a compile-time constant
 *  - constValue:  the compile-time value (if isConst)
 *  - place:       TAC "place" (temp or variable name holding the result)
 */
public final class Expr {

    // -------------------------
    // Inherited attributes
    // -------------------------
    private VariableType expectedType;   // nullable
    private boolean wantLValue;  // e.g., for assignment targets

    // -------------------------
    // Synthesized attributes
    // -------------------------
    private VariableType variableType = VariableType.ERROR;
    private String variableName = "";

    private boolean isLValue;
    private boolean isConst;
    private Object constValue;   // Integer/Boolean/Character/String/etc.
    private String place;        // TAC temp or symbol name

    public Expr() { }

    // --- Inherited setters/getters ---
    public VariableType getExpectedType() {
        return expectedType;
    }

    public Expr setExpectedType(VariableType expectedType) {
        this.expectedType = expectedType;
        return this;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public boolean isWantLValue() {
        return wantLValue;
    }

    public Expr setWantLValue(boolean wantLValue) {
        this.wantLValue = wantLValue;
        return this;
    }

    // --- Synthesized setters/getters ---
    public VariableType getVariableType() {
        return variableType;
    }

    public Expr setVariableType(VariableType variableType) {
        this.variableType = Objects.requireNonNull(variableType);
        return this;
    }

    public boolean isLValue() {
        return isLValue;
    }

    public Expr setLValue(boolean lValue) {
        isLValue = lValue;
        return this;
    }

    public boolean isConst() {
        return isConst;
    }

    public Object getConstValue() {
        return constValue;
    }

    public Expr setConst(Object value, VariableType valueType) {
        this.isConst = true;
        this.constValue = value;
        this.variableType = Objects.requireNonNull(valueType);
        this.isLValue = false; // constants are not assignable
        return this;
    }

    public Expr clearConst() {
        this.isConst = false;
        this.constValue = null;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public Expr setPlace(String place) {
        this.place = place;
        return this;
    }

    public String getExpressionValue() {
        if(variableName!=null && variableName.length()>0) {
            return variableName;
        }
        return getConstValue().toString();
    }


    // -------------------------
    // Convenience checks
    // -------------------------

    /** True if expression can be assigned to (variable, array element, etc.). */
    public boolean isAssignable() {
        return isLValue && variableType != VariableType.ERROR;
    }

    /** True if actual type matches or can be coerced to expected (basic Mini Pascal rules). */
    public boolean isCompatibleWithExpected() {
        if (expectedType == null) return true;
        return VariableType.isAssignable(expectedType, this.variableType);
    }

    @Override
    public String toString() {
        return "Expr{" +
                "expectedType=" + expectedType +
                ", wantLValue=" + wantLValue +
                ", type=" + variableType +
                ", isLValue=" + isLValue +
                ", isConst=" + isConst +
                ", constValue=" + constValue +
                ", place='" + place + '\'' +
                '}';
    }

}