package app;

import java.util.function.Function;

import conts.consts;
import entities.Input;
import entities.Lexema;
import entities.LexemaReader;

public class Main {
    private static char[] input;
    private static LexemaReader lexemaReader;

    public static void main(String[] args) {
        initialize();
    }

    private static void initialize() {
        getInputContent();
        readInputLexemas();
        printLexemas();
    }

    private static void getInputContent() {
        Input file = new Input(consts.FILE_NAME);

        input = file.getFileContent().toCharArray();
    }

    private static void readInputLexemas() {
        lexemaReader = new LexemaReader(input);
    }

    private static void printLexemas() {
        System.out.println("************ LEXEMA ******************\n");

        for (Lexema lexema : lexemaReader.getLexemas()) {
            System.out.println(lexema.getLexema() + " : " + lexema.getToken());
        }
    }
}
