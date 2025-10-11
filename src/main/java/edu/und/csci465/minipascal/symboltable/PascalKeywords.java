package edu.und.csci465.minipascal.symboltable;

import java.util.Set;
import java.util.HashSet;

public class PascalKeywords {
    static Set<String> keywords = new HashSet<>();

    static {
        keywords.add("and");
        keywords.add("array");
        keywords.add("begin");
        keywords.add("char");
        keywords.add("chr");
        keywords.add("div");
        keywords.add("do");
        keywords.add("else");
        keywords.add("end");
        keywords.add("if");
        keywords.add("integer");
        keywords.add("mod");
        keywords.add("not");
        keywords.add("of");
        keywords.add("or");
        keywords.add("ord");
        keywords.add("procedure");
        keywords.add("program");
        keywords.add("read");
        keywords.add("readln");
        keywords.add("then");
        keywords.add("var");
        keywords.add("while");
        keywords.add("write");
        keywords.add("writeln");
    }

    public static Set<String> getMiniPascalKeywords() {
        return keywords;
    }

    public static boolean isKeyword(String str) {
        return keywords.contains(str.toLowerCase());
    }

}