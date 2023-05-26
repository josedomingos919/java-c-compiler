package app;

import conts.Consts;
import entities.Input;
import entities.Lexema;
import entities.LexemaReader;
import entities.SintaticSemanticAnalex;
import java.util.Formatter;

public class Main {

    private static char[] input;
    private static LexemaReader lexemaReader;
    private static SintaticSemanticAnalex sintatic;

    public static void main(String[] args) {
        initialize();
    }

    private static void initialize() {
        getInputContent();
        readInputLexemas();
        sintaticSemanticAnalex();
        printLexemas();
    }

    private static void sintaticSemanticAnalex() {
        sintatic = new SintaticSemanticAnalex(lexemaReader.getLexemas());
    }

    private static void getInputContent() {
        Input file = new Input(Consts.FILE_NAME);

        input = file.getFileContent().toCharArray();
    }

    private static void readInputLexemas() {
        lexemaReader = new LexemaReader(input);
    }

    private static void printLexemas() {
        if (!sintatic.getError().equals("")) {
            System.out.println(sintatic.getError());
            return;
        }

        Formatter fmt = new Formatter();
        fmt.format("%40s %40s %40s\n\n", "LEXEMA", "TOKEN", "LINHA");

        for (Lexema lexema : lexemaReader.getLexemas()) {
            fmt.format("%40s %40s %40s\n\n", lexema.getLexema(), lexema.getToken(), lexema.getLine());
        }

        System.out.println(fmt);
    }
}
