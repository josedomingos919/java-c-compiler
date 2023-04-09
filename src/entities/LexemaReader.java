package entities;

import java.util.ArrayList;
import java.util.regex.*;

import conts.consts;

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
        // semicolon
        else if (Pattern.matches(";", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_9();

            return;
        }
        // igual
        else if (Pattern.matches("=", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_35();

            return;
        }
        // not
        else if (Pattern.matches("!", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_42();

            return;
        }
        // comment
        else if (Pattern.matches("/", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_45();

            return;
        }
        // open parent
        else if (value.equals("(")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_3();

            return;
        }
        // close parent
        else if (value.equals(")")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_36();

            return;
        }
        // open key
        else if (value.equals("{")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_37();

            return;
        }
        // close key
        else if (value.equals("}")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_38();

            return;
        }
        // open parent right
        else if (value.equals("[")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_39();

            return;
        }
        // close parent right
        else if (value.equals("]")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_40();

            return;
        }
        // plus
        else if (value.equals("+")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_10();

            return;
        }
        // comma
        else if (value.equals(",")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_53();

            return;
        }
        // to points
        else if (value.equals(":")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_54();

            return;
        }
        // exclamation match
        else if (value.equals("?")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_55();

            return;
        }
        // simple quotes
        else if (value.equals("'")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_14();

            return;
        }
        // double quotes
        else if (value.equals("\"")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_17();

            return;
        }
        // menus
        else if (value.equals("-")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_19();

            return;
        }
        // smaller
        else if (value.equals("<")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_24();

            return;
        }
        // bigger
        else if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_28();

            return;
        }
        // *
        else if (value.equals("*")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_32();

            return;
        }
        // ignore \n \t \b \r \f \\ espaço em branco
        else if (value.equals(" ") || value.equals("\n") || value.equals("\t") || value.equals("\b")
                || value.equals("\f")
                || value.equals("\r") || value.equals("\\")) {
            increaseIndex();
            initialize();
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

    private void goStatus_10() {
        String value = readInputChar();

        if (value.equals("+")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_11();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_12();
        } else {
            goStatusFinal_13();
        }
    }

    private void goStatusFinal_11() {
        lexemas.add(new Lexema(Token.TK_MA, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_12() {
        lexemas.add(new Lexema(Token.TK_MI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_13() {
        lexemas.add(new Lexema(Token.TK_M, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_14() {
        String value = readInputChar();

        if (value.equals("\\")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_41();
        } else if (!value.equals("")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_15();
        }
    }

    private void goStatus_15() {
        String value = readInputChar();

        if (value.equals("'")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_16();
        }
    }

    private void goStatusFinal_16() {
        lexemas.add(new Lexema(Token.TK_CH, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_17() {
        do {
            String value = readInputChar();

            if (!value.equals("") && !value.equals("\"")) {
                addCharToLexema(value);
                increaseIndex();
            } else if (value.equals("\"")) {
                addCharToLexema(value);
                increaseIndex();
                goStatusFinal_18();

                break;
            } else {
                break;
            }
        } while (true);
    }

    private void goStatusFinal_18() {
        lexemas.add(new Lexema(Token.TK_ST, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_19() {
        String value = readInputChar();

        if (value.equals("-")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_21();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_22();
        } else if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_23();
        } else {
            goStatusFinal_20();
        }
    }

    private void goStatusFinal_20() {
        lexemas.add(new Lexema(Token.TK_MEN, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_21() {
        lexemas.add(new Lexema(Token.TK_MEM, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_22() {
        lexemas.add(new Lexema(Token.TK_MEMI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_23() {
        lexemas.add(new Lexema(Token.TK_MEMM, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_24() {
        String value = readInputChar();

        if (value.equals("<")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_27();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_26();
        } else {
            goStatusFinal_25();
        }
    }

    private void goStatusFinal_25() {
        lexemas.add(new Lexema(Token.TK_MENR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_26() {
        lexemas.add(new Lexema(Token.TK_MENRI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_27() {
        lexemas.add(new Lexema(Token.TK_MMOR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_28() {
        String value = readInputChar();

        if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_29();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_30();
        } else {
            goStatusFinal_31();
        }
    }

    private void goStatusFinal_29() {
        lexemas.add(new Lexema(Token.TK_MAIORM, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_30() {
        lexemas.add(new Lexema(Token.TK_MAIOI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_31() {
        lexemas.add(new Lexema(Token.TK_MAIOR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_32() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_33();
        } else {
            goStatusFinal_34();
        }
    }

    private void goStatusFinal_33() {
        lexemas.add(new Lexema(Token.TK_ASTI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_34() {
        lexemas.add(new Lexema(Token.TK_AST, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_35() {
        lexemas.add(new Lexema(Token.TK_IG, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_36() {
        lexemas.add(new Lexema(Token.TK_FP, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_37() {
        lexemas.add(new Lexema(Token.TK_ABC, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_38() {
        lexemas.add(new Lexema(Token.TK_FCH, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_39() {
        lexemas.add(new Lexema(Token.TK_APR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_40() {
        lexemas.add(new Lexema(Token.TK_FPR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_41() {
        String value = readInputChar();

        if (consts.SPECIAL_CHAR.contains(value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_15();
        }
    }

    private void goStatus_42() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_43();
        } else {
            goStatusFinal_44();
        }
    }

    private void goStatusFinal_43() {
        lexemas.add(new Lexema(Token.TK_NOTI, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_44() {
        lexemas.add(new Lexema(Token.TK_EQUAL, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_45() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_46();
        } else if (value.equals("/")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_48();
        } else if (value.equals("*")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_50();
        } else {
            goStatusFinal_47();
        }
    }

    private void goStatusFinal_46() {
        lexemas.add(new Lexema(Token.TK_BAREQ, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_47() {
        lexemas.add(new Lexema(Token.TK_DIV, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_48() {
        do {
            String value = readInputChar();

            if (value.equals("\r")) {
                increaseIndex();
            } else if (!value.equals("\n") && !value.equals("")) {
                addCharToLexema(value);
                increaseIndex();
            } else {
                break;
            }
        } while (true);

        goStatusFinal_49();
    }

    private void goStatusFinal_49() {
        lexemas.add(new Lexema(Token.TK_COMMENT, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_50() {
        do {
            String value = readInputChar();

            if (!value.equals("*")) {
                addCharToLexema(value);
                increaseIndex();
            } else {
                addCharToLexema(value);
                increaseIndex();
                goStatus_51();

                break;
            }
        } while (true);
    }

    private void goStatus_51() {
        do {
            String value = readInputChar();

            if (value.equals("*")) {
                addCharToLexema(value);
                increaseIndex();
            } else if (value.equals("/")) {
                addCharToLexema(value);
                increaseIndex();
                goStatusFinal_52();

                break;
            } else {
                addCharToLexema(value);
                increaseIndex();
                goStatus_50();

                break;
            }
        } while (true);
    }

    private void goStatusFinal_52() {
        lexemas.add(new Lexema(Token.TK_COMMENTP, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_53() {
        lexemas.add(new Lexema(Token.TK_VIR, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_54() {
        lexemas.add(new Lexema(Token.TK_DOISP, lexema));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_55() {
        lexemas.add(new Lexema(Token.TK_PDE, lexema));

        setLexemaEmpty();
        initialize();
    }

}