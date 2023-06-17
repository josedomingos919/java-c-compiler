package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sintatic {

    int pointer = 0;

    private ArrayList<String> error;
    private ArrayList<Lexema> lexema;

    private final List<String> TYPE_SPEC_ARRAY = Arrays.asList("void", "float", "char", "int", "double");

    public ArrayList<String> getError() {
        return this.error;
    }

    public Lexema getItem() {
        if (this.pointer >= this.lexema.size()) {
            Lexema lastItem = this.lexema.get(this.lexema.size() - 1);

            return new Lexema(Token.TK_END, "END", lastItem.getLine());
        }

        return this.lexema.get(this.pointer);
    }

    public void increasePointer() {
        this.pointer += 1;
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

    public Sintatic(ArrayList<Lexema> lexema) {
        this.error = new ArrayList<>();
        this.lexema = filterDirectives(lexema);

        this.decl();
    }

    // continue
    public void continueAnaliser() {
        Lexema item = getItem();

        if (!item.getToken().equals(Token.TK_END))
            this.decl();
    }

    // error skype
    public void errorSkype() {
        Lexema item = getItem();

        while (!item.getToken().equals(Token.TK_END)) {
            item = getItem();

            if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
                this.decl();
                return;
            } else {
                this.increasePointer();
            }
        }
    }

    public void decl() {
        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.increasePointer();

                this.decl_1();
            } else {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
            }
        } else {
            this.error.add("Esperava receber int, float... na linha: " + item.getLine());
            this.errorSkype();
        }
    }

    public void decl_1() {
        Lexema item = this.getItem();

        if (isVardDeclFirst(item)) {
            this.var_decl();
        } else if (isFunDeclFirst(item)) {
            this.fun_decl();
        } else {
            this.error.add("Esperava receber ; [ = , (  na linha: " + item.getLine());
            this.errorSkype();
        }
    }

    public void var_decl() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_FDI)) {
            this.increasePointer();
            this.continueAnaliser();
        } else if (item.getToken().equals(Token.TK_APR)) {
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_FPR)) {
                this.increasePointer();
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FDI)) {
                    this.increasePointer();
                    this.continueAnaliser();
                } else {
                    this.error.add("Esperava receber ; na linha: " + item.getLine());
                    this.errorSkype();
                }
            } else {
                this.error.add("Esperava receber ] na linha: " + item.getLine());
                this.errorSkype();
            }
        } else if (item.getToken().equals(Token.TK_IG)) {
            this.increasePointer();
            item = this.getItem();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    item = this.getItem();

                    if (item.getToken().equals(Token.TK_FDI)) {
                        this.increasePointer();
                        this.continueAnaliser();
                    } else {
                        this.error.add("Esperava receber ; na linha: " + item.getLine());
                        this.errorSkype();
                    }
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
            }
        } else {
            this.error.add("Esperava receber ; [ = , na linha: " + item.getLine());
            this.errorSkype();
        }
    }

    public void fun_decl() {
    }

    public boolean exp() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_ID)) {
            this.increasePointer();

            if (this.exp_1()) {
                if (this.exp_3()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (item.getLexema().equals("+") || item.getLexema().equals("-") || item.getLexema().equals("!")) {
            this.increasePointer();

            if (this.isExpFirst(item)) {
                if (this.exp()) {
                    if (this.exp_3()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_NF) || item.getToken().equals(Token.TK_NI)
                || item.getToken().equals(Token.TK_CH)) {
            this.increasePointer();

            if (this.exp_3()) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean exp_1() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_APR)) {
            this.increasePointer();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    item = this.getItem();

                    if (item.getToken().equals(Token.TK_FPR)) {
                        this.increasePointer();

                        if (this.exp_2()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        this.error.add("Esperava receber ] na linha: " + item.getLine());
                        this.errorSkype();
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_PONT)) {
            this.increasePointer();
            item = this.getItem();

            if (item.getLexema().equals("sizeof")) {
                return true;
            } else {
                this.error.add("[exp_1] Esperava receber sizeof");
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_AP)) {
            this.increasePointer();

            if (this.args()) {
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FP)) {
                    return true;
                } else {
                    this.error.add("Esperava receber ] na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                return false;
            }
        } else if (item.getToken().equals(Token.TK_MA)) {
            return true;
        } else if (item.getToken().equals(Token.TK_MEM)) {
            return true;
        }

        return true;
    }

    public boolean exp_2() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_IG)) {
            this.increasePointer();

            if (this.isExpFirst(item)) {
                if (this.exp()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean exp_3() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_OR) || item.getToken().equals(Token.TK_IG)
                || item.getToken().equals(Token.TK_IGIG) || item.getToken().equals(Token.TK_MENRI)
                || item.getToken().equals(Token.TK_MENR) || item.getToken().equals(Token.TK_MAIOR)
                || item.getToken().equals(Token.TK_MAIOI) || item.getToken().equals(Token.TK_AND)
                || item.getToken().equals(Token.TK_M) || item.getToken().equals(Token.TK_MEN)
                || item.getToken().equals(Token.TK_PERCENT) || item.getToken().equals(Token.TK_AST)
                || item.getToken().equals(Token.TK_DIV)
                || item.getToken().equals(Token.TK_MI) || item.getToken().equals(Token.TK_MEMI)) {
            this.increasePointer();

            if (this.isExpFirst(item)) {
                if (this.exp()) {
                    if (this.exp_3()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean args() {
        Lexema item = this.getItem();

        if (isExpFirst(item)) {
            if (this.exp()) {
                if (this.arg_list_1()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean arg_list_1() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_VIR)) {
            this.increasePointer();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    if (this.arg_list_1()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int");
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    // validations
    public boolean isVardDeclFirst(Lexema item) {
        return ";[=,".contains(item.getLexema());
    }

    public boolean isFunDeclFirst(Lexema item) {
        return "(".contains(item.getLexema());
    }

    public boolean isExpFirst(Lexema item) {
        return item.getToken().equals(Token.TK_ID) || item.getLexema().equals("+") || item.getLexema().equals("-")
                || item.getLexema().equals("!")
                || item.getToken().equals(Token.TK_NF) || item.getToken().equals(Token.TK_NI)
                || item.getToken().equals(Token.TK_CH);
    }

}
