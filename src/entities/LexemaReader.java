package entities;

import conts.Directives;
import conts.Consts;
import java.util.ArrayList;
import java.util.regex.*;
import conts.Keywords;

public class LexemaReader {
    private int index;
    private int line = 1;
    private int scope = 0;
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

    public void showError(String error) {
        System.out.println("ERRO: na linha " + line + ", " + error);
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
        // #
        else if (value.equals("#")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_71();

            return;
        }
        // one bar
        else if (value.equals("|")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_67();

            return;
        }
        // pow
        else if (value.equals("^")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_82();

            return;
        }
        // percent
        else if (value.equals("%")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_78();

            return;
        }
        // til
        else if (value.equals("~")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_58();

            return;
        }
        // and
        else if (value.equals("&")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_69();

            return;
        }
        // point
        else if (value.equals(".")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_75();

            return;
        }
        // semicolon
        else if (value.equals(";")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_9();

            return;
        }
        // igual
        else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_73();

            return;
        }
        // not
        else if (value.equals("!")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_42();

            return;
        }
        // comment
        else if (value.equals("/")) {
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
            scope++;

            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_37();

            return;
        }
        // close key
        else if (value.equals("}")) {
            scope--;

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
        // ignore \n
        else if (value.equals("\n")) {
            line++;
            increaseIndex();
            initialize();
            return;
        }
        // ignore \n \t \b \r \f \\ espaço em branco
        else if (value.equals(" ")
                || value.equals("\t")
                || value.equals("\b")
                || value.equals("\f")
                || value.equals("\r")
                || value.equals("\\")) {
            increaseIndex();
            initialize();
            return;
        } else {
            System.out.println("Simbolo [" + value + "] não foi reconhecido!");
        }
    }

    private void goStatus_1() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_1();
        } else if (lexema.equals("main")) {
            goStatusFinal_106();
        } else if (Keywords.KEYS.contains(this.lexema)) {
            goStatusFinal_92();
        } else {
            goStatusFinal_2();
        }
    }

    private void goStatusFinal_2() {
        lexemas.add(new Lexema(Token.TK_ID, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_3() {
        lexemas.add(new Lexema(Token.TK_AP, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_NF, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_8() {
        lexemas.add(new Lexema(Token.TK_NI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_9() {
        lexemas.add(new Lexema(Token.TK_FDI, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_MA, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_12() {
        lexemas.add(new Lexema(Token.TK_MI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_13() {
        lexemas.add(new Lexema(Token.TK_M, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_CH, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_ST, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_MEN, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_21() {
        lexemas.add(new Lexema(Token.TK_MEM, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_22() {
        lexemas.add(new Lexema(Token.TK_MEMI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_23() {
        lexemas.add(new Lexema(Token.TK_MEMM, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_24() {
        String value = readInputChar();

        if (value.equals("<")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_65();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_26();
        } else if (value.equals(":")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_85();
        } else if (value.equals("%")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_86();
        } else {
            goStatusFinal_25();
        }
    }

    private void goStatusFinal_25() {
        lexemas.add(new Lexema(Token.TK_MENR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_26() {
        lexemas.add(new Lexema(Token.TK_MENRI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_27() {
        lexemas.add(new Lexema(Token.TK_MMOR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_28() {
        String value = readInputChar();

        if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_63();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_30();
        } else {
            goStatusFinal_31();
        }
    }

    private void goStatusFinal_29() {
        lexemas.add(new Lexema(Token.TK_MAIORM, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_30() {
        lexemas.add(new Lexema(Token.TK_MAIOI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_31() {
        lexemas.add(new Lexema(Token.TK_MAIOR, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_ASTI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_34() {
        lexemas.add(new Lexema(Token.TK_AST, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_35() {
        lexemas.add(new Lexema(Token.TK_IG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_36() {
        lexemas.add(new Lexema(Token.TK_FP, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_37() {
        lexemas.add(new Lexema(Token.TK_ABC, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_38() {
        lexemas.add(new Lexema(Token.TK_FCH, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_39() {
        lexemas.add(new Lexema(Token.TK_APR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_40() {
        lexemas.add(new Lexema(Token.TK_FPR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_41() {
        String value = readInputChar();

        if (Consts.SPECIAL_CHAR.contains(value)) {
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
        lexemas.add(new Lexema(Token.TK_NOTI, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_44() {
        lexemas.add(new Lexema(Token.TK_EQUAL, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_BAREQ, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_47() {
        lexemas.add(new Lexema(Token.TK_DIV, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_COMMENT, lexema, line, scope));

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
        lexemas.add(new Lexema(Token.TK_COMMENTP, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_53() {
        lexemas.add(new Lexema(Token.TK_VIR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_54() {
        lexemas.add(new Lexema(Token.TK_DOISP, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_55() {
        lexemas.add(new Lexema(Token.TK_PDE, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_56() {
        lexemas.add(new Lexema(Token.TK_PONT, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_57() {
        lexemas.add(new Lexema(Token.TK_AND, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_58() {
        lexemas.add(new Lexema(Token.TK_TIL, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_59() {
        lexemas.add(new Lexema(Token.TK_PERCENT, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_60() {
        lexemas.add(new Lexema(Token.TK_POW, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_61() {
        lexemas.add(new Lexema(Token.TK_BAR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_62() {
        lexemas.add(new Lexema(Token.TK_CARD, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_63() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_64();
        } else {
            goStatusFinal_29();
        }
    }

    private void goStatusFinal_64() {
        lexemas.add(new Lexema(Token.TK_MMIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_65() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_66();
        } else {
            goStatusFinal_27();
        }
    }

    private void goStatusFinal_66() {
        lexemas.add(new Lexema(Token.TK_MEMEIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_67() {
        String value = readInputChar();

        if (value.equals("|")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_68();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_84();
        } else {
            goStatusFinal_61();
        }
    }

    private void goStatusFinal_68() {
        lexemas.add(new Lexema(Token.TK_OR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_69() {
        String value = readInputChar();

        if (value.equals("&")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_70();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_81();
        } else {
            goStatusFinal_57();
        }
    }

    private void goStatusFinal_70() {
        lexemas.add(new Lexema(Token.TK_ANDAND, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_71() {
        String value = readInputChar();

        if (value.equals("#")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_72();
        } else if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_80();
        } else if (Pattern.matches("[a-z]", value)) {
            addCharToLexema(value);
            increaseIndex();

            goStatus_93();
        } else {
            goStatusFinal_62();
        }
    }

    private void goStatusFinal_72() {
        lexemas.add(new Lexema(Token.TK_CARDC, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_73() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_74();
        } else {
            goStatusFinal_35();
        }
    }

    private void goStatusFinal_74() {
        lexemas.add(new Lexema(Token.TK_IGIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_75() {
        String value = readInputChar();

        if (value.equals(".")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_76();
        } else {
            goStatusFinal_56();
        }
    }

    private void goStatus_76() {
        String value = readInputChar();

        if (value.equals(".")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_77();
        } else {
            showError("Eperava receber .");
        }
    }

    private void goStatusFinal_77() {
        lexemas.add(new Lexema(Token.TK_PPP, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_78() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_79();
        } else if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_87();
        } else if (value.equals(":")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_88();
        } else {
            goStatusFinal_59();
        }
    }

    private void goStatusFinal_79() {
        lexemas.add(new Lexema(Token.TK_PIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_80() {
        lexemas.add(new Lexema(Token.TK_PIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_81() {
        lexemas.add(new Lexema(Token.TK_ANDE, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_82() {
        String value = readInputChar();

        if (value.equals("=")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_83();
        } else {
            goStatusFinal_60();
        }
    }

    private void goStatusFinal_83() {
        lexemas.add(new Lexema(Token.TK_POIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_84() {
        lexemas.add(new Lexema(Token.TK_BARIG, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_85() {
        lexemas.add(new Lexema(Token.TK_MENPOIN, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_86() {
        lexemas.add(new Lexema(Token.TK_MENPERC, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_87() {
        lexemas.add(new Lexema(Token.TK_PERCMAIOR, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_88() {
        String value = readInputChar();

        if (value.equals("%")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_90();
        } else {
            goStatusFinal_89();
        }
    }

    private void goStatusFinal_89() {
        lexemas.add(new Lexema(Token.TK_PERPOINT, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_90() {
        String value = readInputChar();

        if (value.equals(":")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_91();
        } else {
            showError("Esperava receber :");
        }
    }

    private void goStatusFinal_91() {
        lexemas.add(new Lexema(Token.TK_PERCT2, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatusFinal_92() {
        lexemas.add(new Lexema(Token.TK_KEYWORD, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_93() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_93();
        } else if (Directives.INCLUDE.equals(lexema)) {
            if (value.equals(" ")) {
                addCharToLexema(value);
                increaseIndex();
                goStatus_94();
            } else {
                this.showError("esperava receber um espaço!");
            }
        } else if (Directives.DEFINE.equals(lexema)) {
            if (value.equals(" ")) {
                addCharToLexema(value);
                increaseIndex();
                goStatus_103();
                // goStatus_100();
            } else {
                this.showError("esperava receber um espaço!");
            }
        } else {
            this.showError("esperava receber uma directiva valida!");
        }
    }

    private void goStatus_94() {
        String value = readInputChar();

        if (value.equals("\"")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_95();
        } else if (value.equals("<")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_95();
        } else {
            this.showError("esperava receber \" ou <");
        }
    }

    private void goStatus_95() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_96();
        } else {
            showError("Esperava receber um caracter!");
        }
    }

    private void goStatus_96() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_96();
        } else if (value.equals(".")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_97();
        } else {
            showError("Esperava receber . ou letras");
        }
    }

    private void goStatus_97() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_98();
        } else {
            showError("Esperava receber letras");
        }
    }

    private void goStatus_98() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_98();
        } else if (value.equals("\"")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_99();
        } else if (value.equals(">")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_99();
        } else {
            showError("Esperava receber letras");
        }
    }

    private void goStatusFinal_99() {
        lexemas.add(new Lexema(Token.TK_DIRECTIV, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_100() {
        String value = readInputChar();

        if (value.equals("\"")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_101();
        } else {
            showError("Esperava receber \"");
        }
    }

    private void goStatus_101() {
        String value = readInputChar();

        if (value.equals("\"")) {
            addCharToLexema(value);
            increaseIndex();
            goStatusFinal_102();
        } else {
            addCharToLexema(value);
            increaseIndex();
            goStatus_101();
        }
    }

    private void goStatusFinal_102() {
        lexemas.add(new Lexema(Token.TK_DIRECTIV, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

    private void goStatus_103() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_104();
        } else {
            showError("Esperava receber letra");
        }
    }

    private void goStatus_104() {
        String value = readInputChar();

        if (Pattern.matches("[a-zA-Z]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_104();
        } else if (value.equals(" ")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_100();
        } else {
            showError("Esperava receber letra");
        }
    }

    private void goStatus_105() {
        String value = readInputChar();

        if (Pattern.matches("[a-zAZ]|_|[0-9]", value)) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_104();
        } else if (value.equals(" ")) {
            addCharToLexema(value);
            increaseIndex();
            goStatus_105();
        } else {
            showError("Esperava receber letra");
        }
    }

    private void goStatusFinal_106() {
        lexemas.add(new Lexema(Token.TK_ID, lexema, line, scope));

        setLexemaEmpty();
        initialize();
    }

}