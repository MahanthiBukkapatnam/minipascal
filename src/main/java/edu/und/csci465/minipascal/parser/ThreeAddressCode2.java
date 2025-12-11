package edu.und.csci465.minipascal.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThreeAddressCode2 {

    private List<TACInstr> tac = new ArrayList<>();
    private Set<String> variables = new HashSet<>();

    public ThreeAddressCode2(List<TACInstr> tac, Set<String> variables) {
        this.tac = tac;
        this.variables = variables;
    }

    public void print() {
        printPrologue();
        printBody();
        printEpilogue();
    }

    private void printPrologue() {
        System.out.println("begin_program TAC");
        System.out.println();
        for (String variable : variables) {
            System.out.println("Alloc " + variable + " 4");  //Need to create a TAC for this.
        }
        System.out.println();
    }


    private void printBody() {
        for (TACInstr i : tac) {
            System.out.println(i);
        }
    }

    private void printEpilogue() {
        System.out.println();
        System.out.println("end_program TAC");
    }
}
