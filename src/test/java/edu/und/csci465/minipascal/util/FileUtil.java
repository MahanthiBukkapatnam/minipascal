package edu.und.csci465.minipascal.util;

import edu.und.csci465.minipascal.lexer.InputOutputModule;

public class FileUtil {

    public static void printFile(String fullPath) {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile(fullPath);

        System.out.println("");
        System.out.println("Printing original file: " + fullPath);
        System.out.println("--------------------------------------------");
        System.out.println(ioModule.getFileContent());
        System.out.println("--------------------------------------------");
    }
}
