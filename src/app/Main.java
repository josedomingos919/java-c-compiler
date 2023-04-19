package app;

import conts.consts;
import entities.Input;
import entities.Lexema;
import entities.LexemaReader;
import java.util.Formatter; 

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
        Formatter fmt = new Formatter();  
        fmt.format("%40s %40s %40s\n\n", "LEXEMA", "TOKEN", "LINHA");

        for (Lexema lexema : lexemaReader.getLexemas()) {
            fmt.format("%40s %40s %40s\n\n", lexema.getLexema(), lexema.getToken(), lexema.getLine()); 
        }

        System.out.println(fmt);
    }
}
