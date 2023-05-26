package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SintaticSemanticAnalex {

    private ArrayList<Lexema> lexema;
    private int pointer = 0;
    private Lexema readedLexema;
    private List<String> TYPE_SPEC_ARRAY = Arrays.asList("void", "float", "char", "int", "double");
    private String error = "";

    public String getError() {
        return this.error;
    }

    public Lexema readLexema() {
        if (this.pointer < lexema.size()) {
            this.readedLexema = lexema.get(this.pointer);
            this.pointer = this.pointer + 1;
            return this.readedLexema;
        }

        return new Lexema("", "", -1);
    }

    public void printError(String text) {
        this.error = text + " na linha " + this.readedLexema.getLine();
    }

    public SintaticSemanticAnalex(ArrayList<Lexema> lexema) {
        this.lexema = lexema;
        program();
    }

    public void program() {
        decl_list();
    }

    public void decl_list() {
        this.decl();
        this.decl_list_1();
    }

    public void decl() {
        if (this.var_decl()) {
            System.out.println("Declaração de variavel válida");
        } else if (this.fun_decl()) {
            System.out.println("Declaração de funução válida");
        } else {
            // System.out.println("Não declarou nada");
        }
    }

    public boolean var_decl() {
        return this.type_spec();
    }

    public boolean type_spec() {
        Lexema item = readLexema();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            return this.type_spec_id();
        } else {
            pointer--;
            this.printError("Esperava receber um tipo de dados int, float, char... ");
            return false;
        }
    }

    public boolean type_spec_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID) || item.getToken().equals(Token.TK_VAR)) {
            if (type_spec_id_semicolon()) {
                return true;
            } else if (type_spec_id_open_direct_relatives()) {
                return true;
            } else {
                return false;
            }
        } else {
            pointer--;
            this.printError("Esperava receber um identificador ou uma variavel ");
            return false;
        }
    }

    public boolean type_spec_id_semicolon() {
        Lexema item = readLexema();

        if (item.getLexema().equals(";")) {
            return true;
        } else {
            pointer--;
            return false;
        }
    }

    public boolean type_spec_id_open_direct_relatives() {
        Lexema item = readLexema();

        if (item.getLexema().equals("[")) {
            if (this.type_spec_id_close_direct_relatives()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean type_spec_id_close_direct_relatives() {
        Lexema item = readLexema();

        if (item.getLexema().equals("]")) {
            return type_spec_id_close_direct_relatives_close_semicolo();
        } else {
            this.printError("Esperava receber ] ");
            return false;
        }
    }

    public boolean type_spec_id_close_direct_relatives_close_semicolo() {
        Lexema item = readLexema();

        if (item.getLexema().equals(";")) {
            return true;
        } else {
            this.printError("Esperava receber => ; ");
            return false;
        }
    }

    public boolean fun_decl() {
        return true;
    }

    public void decl_list_1() {

    }
}
