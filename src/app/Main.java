package app;

import conts.Consts;
import entities.Input;
import entities.Lexema;
import entities.Sintatic;
import entities.Semantic;
import java.util.Formatter;
import entities.LexemaReader;
import entities.SemanticReader;

public class Main {
    private static char[] input;
    private static Sintatic sintatic;
    private static LexemaReader lexemaReader;
    private static SemanticReader semanticReader;

    public static void main(String[] args) {
        initialize();
    }

    private static void initialize() {
        getInputContent();
        readInputLexemas();
        sintaticAnalex();
        printSintaticError();

        if (sintatic.getError().size() == 0) {
            printSemanticTable();
            semanticAnalex();
            printSemanticError();

            if (semanticReader.getErros().size() == 0) {
                printLexemas();
            }
        }
    }

    private static void sintaticAnalex() {
        sintatic = new Sintatic(lexemaReader.getLexemas());
    }

    private static void semanticAnalex() {
        semanticReader = new SemanticReader(sintatic.getSemanticTable());
    }

    private static void printSemanticError() {
        for (String error : semanticReader.getErros()) {
            System.out.println(error);
        }

        if (semanticReader.getErros().size() > 0)
            System.out.println("Total de erros: " + semanticReader.getErros().size());
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
        fmt.format("%40s %40s %40s\n\n", "", "************Lexema************", "");
        fmt.format("%40s %40s %40s\n\n", "LEXEMA", "TOKEN", "LINHA");

        for (Lexema lexema : lexemaReader.getLexemas()) {
            fmt.format("%40s %40s %40s\n\n", lexema.getLexema(), lexema.getToken(), lexema.getLine());
        }

        System.out.println(fmt);
    }

    private static void printSemanticTable() {
        Formatter fmt = new Formatter();
        fmt.format("%40s %40s %40s\n\n", "", "************Semantico************", "");
        fmt.format("%40s %40s %40s\n\n", "TIPO", "ESCOPO", "EXPRESSAO");

        for (Semantic semantic : sintatic.getSemanticTable()) {
            String expression = "";
            int scope = -1;

            int i = 0;
            for (Lexema lexema : semantic.getSignature()) {
                expression += lexema.getLexema() + " ";

                if (i == 0)
                    scope = lexema.getScope();

                i++;
            }

            fmt.format("%40s %40s %40s\n\n", semantic.getType(), scope, expression);
        }

        System.out.println(fmt);
    }
}
