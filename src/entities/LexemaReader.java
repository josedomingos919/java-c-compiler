package entities;

import java.util.ArrayList;
import java.util.regex.*;

public class LexemaReader {

    private int index;
    public char[] input;
    private String lexema;
    private ArrayList<Lexema> lexemas;

    public LexemaReader(char[] input) {
        this.input = input;
        this.index = 0;
        this.lexema = "";
        this.lexemas = new ArrayList<>();

        initialize();
    }

    // functions
    public void initialize() {
        if (canReadChar()) {
            goStatusInitial_0();
        }
    }

    private boolean canReadChar() {
        return index < input.length;
    }

    private void addCharToLexema(String value) {
        lexema += value;
    }

    private void increaseIndex() {
        index = index + 1;
    }

    private void setLexemaEmpty() {
        this.lexema = "";
    }

    private String readInputChar() {
        if (index < input.length) {
            return String.valueOf(input[index]);
        } else {
            return "";
        }
    }

    public ArrayList<Lexema> getLexemas() {
        return this.lexemas;
    }

    // status
    private void goStatusInitial_0() {
        String value = readInputChar();

        // var
        if (Pattern.matches("[a-zA-Z]|_", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_1();

            return;
        }
        // number
        else if (Pattern.matches("[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_4();

            return;
        }
        // number
        else if (Pattern.matches(";", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_9();

            return;
        }
        // open parent
        else if (value.equals("(")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_3();

            return;
        } // comment
        else if (value.equals("/")) {
            addCharToLexema(value);
            increaseIndex();

            return;
        }
    }

    private void goStatus_1() {
        do {
            String value = readInputChar();

            if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
                addCharToLexema(value);
                increaseIndex();
            } else {
                break;
            }
        } while (true);

        goStatusFinal_2();
    }

    private void goStatusFinal_2() {
        lexemas.add(new Lexema(Token.TK_ID, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_3() {
        lexemas.add(new Lexema(Token.TK_AP, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_4() {
        do {
            String value = readInputChar();

            if (Pattern.matches("[0-9]", value)) {
                addCharToLexema(value);
                increaseIndex();
            } else if (value.equals(".")) {
                addCharToLexema(value);
                increaseIndex();

                goStatus_5();
                break;
            } else {
                goStatusFinal_8();
                break;
            }
        } while (true);
    }

    private void goStatus_5() {
        String value = readInputChar();

        if (Pattern.matches("[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_6();
        }
    }

    private void goStatus_6() {
        do {
            String value = readInputChar();

            if (Pattern.matches("[0-9]", value)) {
                addCharToLexema(value);
                increaseIndex();
            } else {
                break;
            }
        } while (true);

        goStatusFinal_7();
    }

    private void goStatusFinal_7() {
        lexemas.add(new Lexema(Token.TK_NF, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_8() {
        lexemas.add(new Lexema(Token.TK_NI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_9() {
        lexemas.add(new Lexema(Token.TK_FDI, lexema));

        setLexemaEmpty();
        initialize();
    }
}
