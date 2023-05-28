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
        if (this.pointer >= 0 && lexema.size() > 0 && this.pointer < lexema.size() && this.error.equals("")) {
            this.readedLexema = lexema.get(this.pointer);
            this.pointer = this.pointer + 1;
            return this.readedLexema;
        }

        this.pointer = -1;
        return new Lexema("", "", -1);
    }

    public void printError(String text) {
        if (this.error.equals("") && this.readedLexema != null) {
            this.error = text + " na linha " + this.readedLexema.getLine();
        }
    }

    public ArrayList<Lexema> filterDirectives(ArrayList<Lexema> lexema) {
        ArrayList<Lexema> newLexema = new ArrayList<Lexema>();

        int index = 0;

        for (Lexema item : lexema) {
            if (!item.getToken().equals(Token.TK_DIRECTIV) &&
                    !item.getToken().equals(Token.TK_CARD) &&
                    !item.getToken().equals(Token.TK_CARDC)) {
                item.setIndex(index);
                newLexema.add(item);
            }

            index++;
        }

        return newLexema;
    }

    public SintaticSemanticAnalex(ArrayList<Lexema> lexema) {
        this.lexema = filterDirectives(lexema);
        program();
    }

    public void program() {
        decl_list();
    }

    public void decl_list() {
        this.decl();
    }

    public void decl() {
        if (this.var_decl()) {
            // System.out.println("Declaraco de variavel valida");

            if (this.pointer < this.lexema.size()) {
                this.decl();
            }
        } else if (this.fun_decl()) {
            // System.out.println("Declaracao de funcao valida");

            if (this.pointer < this.lexema.size()) {
                this.decl();
            }
        } else {
            // System.out.println("***Fim de programa***");
        }
    }

    public boolean var_decl() {
        if (this.type_spec()) {
            return true;
        } else {
            this.pointer -= 1;
            return false;
        }
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
            this.printError("[type_spec] Esperava receber um tipo de dados int, float, char... ");
            return false;
        }
    }

    public boolean type_spec_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID) || item.getToken().equals(Token.TK_VAR)) {
            item = readLexema();

            if (item.getToken().equals(Token.TK_FDI)) {
                return true;
            } else if (item.getToken().equals(Token.TK_APR)) {
                item = readLexema();

                if (item.getToken().equals(Token.TK_FPR)) {
                    item = readLexema();

                    if (item.getToken().equals(Token.TK_FDI)) {
                        return true;
                    } else {
                        this.pointer -= 1;
                        this.printError("[type_spec_id] Esperava receber ;");
                        return false;
                    }
                } else {
                    this.pointer -= 1;
                    this.printError("[type_spec_id] Esperava receber ]");
                    return false;
                }
            } else if (item.getToken().equals(Token.TK_IG)) {
                if (this.exp()) {
                    item = readLexema();

                    if (item.getToken().equals(Token.TK_FDI)) {
                        return true;
                    } else {
                        this.pointer -= 1;
                        this.printError("[type_spec_id-exp] Esperava receber ;");
                        return false;
                    }
                } else {
                    this.printError("[type_spec_id-exp] Expressao invalida");
                    return false;
                }
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            this.printError("[type_spec_id] Esperava receber um identificador ou uma variavel ");
            return false;
        }
    }

    public boolean fun_decl() {
        Lexema item = readLexema();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            return this.fun_decl_id();
        } else {
            this.printError("[fun_decl] Esperava receber um tipo de dados ");
            pointer--;
            return false;
        }
    }

    public boolean fun_decl_id() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ID) || item.getToken().equals(Token.TK_VAR)) {
            return this.fun_decl_id_open_parent();
        } else {
            this.printError("[fun_decl_id] Esperava receber um ID ");
            this.pointer -= 1;
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

        this.printError("[fun_decl_id_open_parent] Esperava receber ( ");
        this.pointer -= 1;
        return false;
    }

    public boolean fun_decl_id_close_parent() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FP)) {
            return true;
        } else {
            this.printError("[fun_decl_id_close_parent] Esperava receber ) ");
            this.pointer -= 1;
            return false;
        }
    }

    public boolean fun_decl_id_open_parent_params() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_VOID)) {
            return true;
        } else {
            this.pointer -= 1;
        }

        if (this.param_list_2()) {
            return true;
        } else {
            this.printError("[fun_decl_id_open_parent_params]  Espera receber um parametro ou void ");
            this.pointer -= 1;
            return false;
        }
    }

    public boolean fun_decl_1() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FDI)) {
            return true;
        } else {
            this.pointer -= 1;
            return this.com_stmt(false);
        }
    }

    public boolean com_stmt(boolean isOptional) {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_ABC)) {
            if (this.local_decls()) {
                if (this.stmt_list()) {
                    item = readLexema();

                    if (item.getToken().equals(Token.TK_FCH)) {
                        return true;
                    } else {
                        if (!isOptional)
                            this.printError("[com_stmt] Esperava receber } ");
                        this.pointer -= 1;
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
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean if_stmt() {
        Lexema item = readLexema();

        if (item.getLexema().equals("if")) {
            item = readLexema();

            if (item.getToken().equals(Token.TK_AP)) {

                if (this.exp()) {
                    item = readLexema();

                    if (item.getToken().equals(Token.TK_FP)) {
                        if (this.stmt()) {
                            if (this.if_stmt_1()) {
                                return true;
                            } else {
                                this.printError("[if_stmt] Expressão inválida");
                                return false;
                            }
                        } else {
                            this.printError("[if_stmt] Expressão inválida");
                            return false;
                        }
                    } else {
                        this.printError("[if_stmt] Esperava receber )");
                        this.pointer -= 1;
                        return false;
                    }
                } else {
                    this.printError("[if_stmt] Expresão invalida");
                    return false;
                }
            } else {
                this.printError("[if_stmt] Esperava receber (");
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean if_stmt_1() {
        Lexema item = readLexema();

        if (item.getLexema().equals("else")) {
            if (this.stmt()) {
                return true;
            } else {
                return false;
            }
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean while_stmt() {
        Lexema item = readLexema();

        if (item.getLexema().equals("while")) {
            item = readLexema();

            if (item.getToken().equals(Token.TK_AP)) {
                if (this.exp()) {

                    item = readLexema();

                    if (item.getToken().equals(Token.TK_FP)) {
                        if (this.stmt()) {
                            return true;
                        } else {
                            this.printError("[while_stmt-stmt] Expressão invalida");
                            return false;
                        }
                    } else {
                        this.printError("[while_stmt] Esperava receber ) ");
                        return false;
                    }
                } else {
                    this.printError("[while_stmt-exp] Expressão invalida");
                    return false;
                }
            } else {
                this.printError("[while_stmt] Esperava receber (");
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean stmt() {
        if (this.exp_stmt()) {
            return true;
        } else if (this.com_stmt(true)) {
            return true;
        } else if (this.if_stmt()) {
            return true;
        } else if (this.while_stmt()) {
            return true;
        } else if (this.return_stmt()) {
            return true;
        } else if (this.break_stmt()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean break_stmt() {
        Lexema item = this.readLexema();

        if (item.getToken().equals("break")) {
            item = this.readLexema();

            if (item.getLexema().equals(";")) {
                return true;
            } else {
                this.pointer -= 1;
                this.printError("[break_stmt] Esperava receber ;");
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean return_stmt() {
        Lexema item = this.readLexema();

        if (item.getLexema().equals("return")) {
            if (this.return_stmt_1()) {
                return true;
            } else {
                this.printError("[return_stmt_1] Expressão invalida");
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean return_stmt_1() {
        Lexema item = this.readLexema();

        if (item.getLexema().equals(";")) {
            return true;
        } else {
            this.pointer -= 1;
        }

        if (this.exp()) {
            item = this.readLexema();

            if (item.getLexema().equals(";")) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean exp_stmt() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_FDI)) {
            return true;
        } else {
            this.pointer -= 1;
        }

        if (this.exp()) {
            item = this.readLexema();

            if (item.getToken().equals(Token.TK_FDI)) {
                return true;
            } else {
                this.pointer -= 1;
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean stmt_list() {
        if (this.stmt()) {
            return this.stmt_list();
        } else {
            return true;
        }
    }

    public boolean exp() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_ID)) {
            if (this.exp_1()) {
                if (this.exp_3()) {
                    return true;
                } else {
                    this.printError("[exp-exp_3] Expressão invalida");
                    return false;
                }
            } else {
                this.printError("[exp-exp_1] Expressão invalida");
                return false;
            }
        } else if (item.getLexema().equals("+") || item.getLexema().equals("-") || item.getLexema().equals("!")) {
            if (this.exp()) {
                if (this.exp_3()) {
                    return true;
                } else {
                    this.printError("[exp-exp-exp_3] Expressão invalida");
                    return false;
                }
            } else {
                this.printError("[exp-exp] Expressão invalida");
                return false;
            }
        } else if (item.getToken().equals(Token.TK_NF) || item.getToken().equals(Token.TK_NI)
                || item.getToken().equals(Token.TK_CH)) {
            if (this.exp_3()) {
                return true;
            } else {
                this.printError("[exp-exp_3] Expressão invalida");
                return false;
            }
        } else {
            this.pointer -= 1;
            return false;
        }
    }

    public boolean exp_3() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_OR) || item.getToken().equals(Token.TK_IG)
                || item.getToken().equals(Token.TK_IGIG) || item.getToken().equals(Token.TK_MENRI)
                || item.getToken().equals(Token.TK_MENR) || item.getToken().equals(Token.TK_MAIOR)
                || item.getToken().equals(Token.TK_MAIOI) || item.getToken().equals(Token.TK_AND)
                || item.getToken().equals(Token.TK_M) || item.getToken().equals(Token.TK_MEN)
                || item.getToken().equals(Token.TK_PERCENT) || item.getToken().equals(Token.TK_AST)
                || item.getToken().equals(Token.TK_DIV)) {
            if (this.exp()) {
                if (this.exp_3()) {
                    return true;
                } else {
                    this.printError("[exp_3] Expressão inválida");
                    return false;
                }
            } else {
                this.printError("[exp_3] Expressão inválida");
                return false;
            }
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean exp_1() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_APR)) {
            if (this.exp()) {
                item = this.readLexema();

                if (item.getToken().equals(Token.TK_FPR)) {
                    if (this.exp_2()) {
                        return true;
                    } else {
                        this.printError("[exp_1-exp-exp_2] Expressão invalida");
                        return false;
                    }
                } else {
                    this.printError("[exp_1-exp-exp_2] Esperava receber ]");
                    this.pointer -= 1;
                    return false;
                }
            } else {
                this.printError("[exp_1-exp] Esperava receber ]");
                return false;
            }
        } else if (item.getToken().equals(Token.TK_PONT)) {
            item = this.readLexema();

            if (item.getLexema().equals("sizeof")) {
                return true;
            } else {
                this.printError("[exp_1] Esperava receber sizeof");
                this.pointer -= 1;
                return true;
            }
        } else if (item.getToken().equals(Token.TK_AP)) {
            if (this.args()) {
                item = this.readLexema();

                if (item.getToken().equals(Token.TK_FP)) {
                    return true;
                } else {
                    this.pointer -= 1;
                    this.printError("[exp_1-args] Esperava receber ]");
                    return false;
                }
            } else {
                this.printError("[exp_1-args] Expressão inválida");
                return false;
            }
        }

        this.pointer -= 1;
        return true;
    }

    public boolean exp_2() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_IG)) {
            if (this.exp()) {
                return true;
            } else {
                this.printError("[exp_2] Expressão invalida!");
                return false;
            }
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean args() {
        if (this.arg_list()) {
            return true;
        } else {
            return true;
        }
    }

    public boolean arg_list() {
        if (this.exp()) {
            if (this.arg_list_1()) {
                return true;
            } else {
                this.printError("[arg_list-arg_list_1] Expressão invalida!");
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean arg_list_1() {
        Lexema item = this.readLexema();

        if (item.getToken().equals(Token.TK_VIR)) {
            if (this.exp()) {
                if (this.arg_list_1()) {
                    return true;
                } else {
                    this.printError("[arg_list_1] Argumento Inválida!");
                    return false;
                }
            } else {
                this.printError("[arg_list_1-exp] Expressão Inválida!");
                return false;
            }
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean local_decls() {
        Lexema item = readLexema();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            item = readLexema();

            if (item.getToken().equals(Token.TK_ID)) {
                if (this.local_decl_1()) {
                    return true;
                } else {
                    this.pointer -= 1;
                    return false;
                }
            } else {
                this.printError("[local_decls] Esperava receber um ID");
                this.pointer -= 1;
                return false;
            }
        } else {
            this.pointer -= 1;
            return true;
        }
    }

    public boolean local_decl_1() {
        Lexema item = readLexema();

        if (item.getToken().equals(Token.TK_FDI)) {
            return this.local_decls();
        } else if (item.getToken().equals(Token.TK_APR)) {
            item = readLexema();

            if (item.getToken().equals(Token.TK_FPR)) {
                item = readLexema();

                if (item.getToken().equals(Token.TK_FDI)) {
                    return this.local_decls();
                } else {
                    this.printError("Esperava receber ;");
                    this.pointer -= 1;
                    return false;
                }
            } else {
                this.printError("Esperava receber ]");
                this.pointer -= 1;
                return false;
            }
        } else if (item.getToken().equals(Token.TK_IG)) {
            if (this.exp()) {
                item = readLexema();

                if (item.getToken().equals(Token.TK_FDI)) {
                    return this.local_decls();
                } else {
                    this.pointer -= 1;
                    this.printError("[local_decl_1-exp] Esperava receber ;");
                    return false;
                }
            } else {
                this.printError("[local_decl_1-exp] Expressao invalida");
                return false;
            }
        } else {
            this.pointer -= 1;
            this.printError("[local_decl_1] Esperava receber ; ou [ ");
            return false;
        }
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
            this.printError("[param_type_spec_id_param_1_close_parent] Esperava receber ] ");
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
                    this.printError("[param_list_1] Esperava receber um parametro ");
                }

                return false;
            }
        } else {

            this.pointer -= 1;
            return true;
        }
    }

}
