package edu.und.csci465.minipascal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PascalOperatorCharacter {
    static Map<String, String> keywords = new HashMap<>();

    static {
        keywords.put(".","PERIOD");

        keywords.put("+","PLUS");
        keywords.put("-","MINUS");
        keywords.put("*","ASTERISK");
        keywords.put("/","DIVISION");

        keywords.put("=","EQUAL");
        keywords.put("<","LESS_THAN");
        keywords.put(">","GREATER_THAN");
        keywords.put("(","LEFT_PARANTHESIS");
        keywords.put(")","RIGHT_PARANTHESIS");
        keywords.put("[","LEFT_SQUARE_BRACKET");
        keywords.put("]","RIGHT_SQUARE_BRACKET");

        keywords.put(":","COLON");
        keywords.put(";","SEMI-COLON");
        keywords.put(",","COMMA");
    }

    public static Set<String> getMiniPascalOperatorCharacter() {
        return keywords.keySet();
    }

    public static boolean isKeyword(char str) {
        return keywords.keySet().contains(""+str);
    }

    public static String getOperatorName(char ch1, char ch2) {
        if(isLessThanOrEqual(ch1,ch2)) {
            return "OP_LT_OR_EQUAL";
        }
        if(isNotEqual(ch1,ch2)) {
            return "OP_NOT_EQUAL";
        }
        if(isGreaterThanOrEqual(ch1,ch2)) {
            return "OP_GREATER_THAN_OR_EQUAL";
        }
        if(isAssignment(ch1,ch2)) {
            return "OP_ASSIGNMENT";
        }

        return keywords.get(""+ch1);
    }
    
    
    public static boolean isKeyword(char ch1, char ch2) {
        if(isLessThanOrEqual(ch1,ch2)) {
            return true;
        }
        if(isNotEqual(ch1,ch2)) {
            return true;
        }
        if(isGreaterThanOrEqual(ch1,ch2)) {
            return true;
        }
        if(isAssignment(ch1,ch2)) {
            return true;
        }

        return keywords.keySet().contains(""+ch1);
    }

    public static boolean isLessThanOrEqual(char ch1, char ch2) {
        return ch1 == '<' && ch2 == '=';
    }
    public static boolean isNotEqual(char ch1, char ch2) {
        return ch1 == '<' && ch2 == '>';
    }
    public static boolean isGreaterThanOrEqual(char ch1, char ch2) {
        return ch1 == '>' && ch2 == '=';
    }
    public static boolean isAssignment(char ch1, char ch2) {
        return ch1 == ':' && ch2 == '=';
    }
}