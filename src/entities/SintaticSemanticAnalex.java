package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SintaticSemanticAnalex {

    int pointer = 0;
    private String error = "";
    private Lexema readedLexema;
    private ArrayList<Lexema> lexema;

    private final List<String> TYPE_SPEC_ARRAY = Arrays.asList("void", "float", "char", "int", "double");

    public String getError() {
        return this.error;
    }

    public Lexema readLexema() {
        if (this.pointer < lexema.size() && this.error.equals("")) {
            this.readedLexema = lexema.get(this.pointer);
            this.pointer = this.pointer + 1;
            return this.readedLexema;
        }

        return new Lexema("", "", -1);
    }

    public void printError(String text) {
        if (this.error.equals("")) {
            this.error = text + " na linha " + this.readedLexema.getLine();
        }
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

            if (this.pointer < this.lexema.size()) {
                this.decl();
            }
        } else if (this.fun_decl()) {
            if (this.pointer < this.lexema.size()) {
                this.decl();
            }
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
            if (this.type_spec_id()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            this.printError("Esperava receber um tipo de dados int, float, char... ");
            return false;
        }
    }

    public boolean type_spec_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID) || item.getToken().equals(Token.TK_VAR)) {
            if (this.type_spec_id_semicolon()) {
                return true;
            } else if (this.type_spec_id_open_direct_relatives()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            this.printError("Esperava receber um identificador ou uma variavel ");
            return false;
        }
    }

    public boolean type_spec_id_semicolon() {
        Lexema item = readLexema();

        if (item.getLexema().equals(";")) {
            return true;
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean type_spec_id_open_direct_relatives() {
        Lexema item = readLexema();

        if (item.getLexema().equals("[")) {
            if (this.type_spec_id_close_direct_relatives()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean type_spec_id_close_direct_relatives() {
        Lexema item = readLexema();

        if (item.getLexema().equals("]")) {
            if (type_spec_id_close_direct_relatives_close_semicolo()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            this.printError("Esperava receber ] ");
            return false;
        }
    }

    public boolean type_spec_id_close_direct_relatives_close_semicolo() {
        Lexema item = readLexema();

        if (item.getLexema().equals(";")) {
            return true;
        } else {
            this.pointer -= 1;
            this.printError("Esperava receber => ; ");
            return false;
        }
    }

    public boolean fun_decl() {
        Lexema item = readLexema();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            return this.fun_decl_id();
        } else {
            this.printError("Esperava receber um tipo de dados ");
            pointer--;
            return false;
        }
    }

    public boolean fun_decl_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID)) {
            return this.fun_decl_id_open_parent();
        } else {
            this.printError("Esperava receber um ID ");
            this.pointer = this.pointer - 1;
            return false;
        }
    }

    public boolean fun_decl_id_open_parent() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_AP)) {
            if (this.fun_decl_id_open_parent_params()) {
                if (this.fun_decl_id_close_parent()) {
                    if (this.fun_decl_1()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    this.pointer -= 1;
                    return false;
                }
            } else {
                this.pointer -= 1;
                return false;
            }
        }

        this.printError("Esperava receber ( ");
        this.pointer -= 1;
        return false;
    }

    public boolean fun_decl_id_close_parent() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FP)) {
            return true;
        } else {
            this.printError("Esperava receber ) ");
            this.pointer -= 1;
            return false;
        }
    }

    public boolean fun_decl_id_open_parent_params() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_VOID)) {
            return true;
        } else if (item.getToken().equals(Token.TK_FP)) {
            return true;
        } else {
            this.pointer -= 1;
        }

        if (this.param_list_2()) {
            return true;
        } else {
            this.printError("Espera receber um parametro ou void ou ) ");
            this.pointer -= 1;
            return false;
        }
    }

    public boolean fun_decl_1() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FDI)) {
            return true;
        } else {
            return this.com_stmt();
        }
    }

    public boolean com_stmt() {
        return false;
    }

    public boolean param_list_2() {
        if (this.param()) {
            return this.param_list_1();
        } else {
            return true;
        }
    }

    public boolean param() {
        Lexema item = readLexema();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            if (param_type_spec_id()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean param_type_spec_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID)) {
            if (this.param_type_spec_id_param_1()) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean param_type_spec_id_param_1() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_APR)) {
            return this.param_type_spec_id_param_1_close_parent();
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean param_type_spec_id_param_1_close_parent() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FPR)) {
            return true;
        } else {
            this.printError("Esperava receber ] ");
            this.pointer -= 1;
            return false;
        }
    }

    public boolean param_list_1() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_VIR)) {
            if (this.param()) {
                return this.param_list_1();
            } else {
                if (this.error.equals("")) {
                    this.printError("Esperava receber um parametro ");
                }

                return false;
            }
        } else {

            this.pointer -= 1;
            return true;
        }
    }

    public void decl_list_1() {
    }
}
