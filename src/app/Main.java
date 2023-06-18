package app;

import conts.Consts;
import entities.Input;
import entities.Lexema;
import java.util.Formatter;
import entities.LexemaReader;
import entities.Sintatic;

public class Main {
    private static char[] input;
    private static LexemaReader lexemaReader;
    private static Sintatic sintatic;

    public static void main(String[] args) {
        initialize();
    }

    private static void initialize() {
        getInputContent();
        readInputLexemas();
        sintaticSemanticAnalex();
        printSintaticError();

        if (sintatic.getError().size() == 0)
            printLexemas();
    }

    private static void sintaticSemanticAnalex() {
        sintatic = new Sintatic(lexemaReader.getLexemas());
    }

    private static void printSintaticError() {
        for (String error : sintatic.getError()) {
            System.out.println(error);
        }

        if (sintatic.getError().size() > 0)
            System.out.println("Total de erros: " + sintatic.getError().size());
    }

    private static void getInputContent() {
        Input file = new Input(Consts.FILE_NAME);

        input = file.getFileContent().toCharArray();
    }

    private static void readInputLexemas() {
        lexemaReader = new LexemaReader(input);
    }

    private static void printLexemas() {

        Formatter fmt = new Formatter();
        fmt.format("%40s %40s %40s\n\n", "LEXEMA", "TOKEN", "LINHA");

        for (Lexema lexema : lexemaReader.getLexemas()) {
            fmt.format("%40s %40s %40s\n\n", lexema.getLexema(), lexema.getToken(), lexema.getLine());
        }

        System.out.println(fmt);
    }
}
