package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sintatic {

    private int pointer = 0;
    private ArrayList<String> error;
    private ArrayList<Lexema> lexema;
    private ArrayList<Lexema> expression;
    private ArrayList<Semantic> semanticTable;
    private final List<String> TYPE_SPEC_ARRAY = Arrays.asList("void", "float", "char", "int", "double");

    public ArrayList<String> getError() {
        return this.error;
    }

    public ArrayList<Semantic> getSemanticTable() {
        return this.semanticTable;
    }

    public Lexema getItem() {
        if (this.pointer >= this.lexema.size()) {
            Lexema lastItem = this.lexema.get(this.lexema.size() - 1);

            return new Lexema(Token.TK_END, "END", lastItem.getLine(), lastItem.getScope());
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
                    !item.getToken().equals(Token.TK_CARDC) &&
                    !item.getToken().equals(Token.TK_COMMENT) &&
                    !item.getToken().equals(Token.TK_COMMENTP)) {
                item.setIndex(index);
                newLexema.add(item);
            }

            index++;
        }

        return newLexema;
    }

    private void clearExpression() {
        this.expression = new ArrayList<>();
    }

    private void addExpression(Lexema item) {
        this.expression.add(item);
    }

    private void saveExpression(ArrayList<Lexema> expression, String type) {
        Semantic semantic = new Semantic(expression, type);

        semanticTable.add(semantic);

        this.clearExpression();
    }

    public Sintatic(ArrayList<Lexema> lexema) {
        this.error = new ArrayList<>();
        this.lexema = filterDirectives(lexema);
        this.semanticTable = new ArrayList<>();

        this.decl();
    }

    // continue
    public void continueAnaliser() {
        Lexema item = getItem();

        if (item.getToken().equals(Token.TK_END))
            return;

        if (item.getToken().equals(Token.TK_FCH)) {
            this.increasePointer();
            this.continueAnaliser();
            return;
        }

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.decl();
            return;
        }

        if (isKeyWord(item) || item.getToken().equals(Token.TK_ID)) {
            this.stmt();
            this.continueAnaliser();
            return;
        }

        this.error.add("Esperava receber int float ou if for... na linha: " + item.getLine());
        this.errorSkype();
    }

    // error skype
    public void errorSkype() {
        Lexema item = getItem();

        while (!item.getToken().equals(Token.TK_END)) {
            item = getItem();

            if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
                this.decl();
                return;
            } else if (item.getToken().equals(Token.TK_ABC)) {
                this.com_stmt(false);
                return;
            } else if (this.isKeyWord(item) || item.getToken().equals(Token.TK_ID)) {
                this.stmt();
                errorSkype();
                return;
            } else {
                this.increasePointer();
            }
        }
    }

    public void decl() {
        this.clearExpression();

        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.addExpression(item);
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
            this.addExpression(item);
            this.saveExpression(expression, "var_decl");

            this.increasePointer();
            this.continueAnaliser();
        } else if (item.getToken().equals(Token.TK_VIR)) {
            this.addExpression(item);
            this.saveExpression(expression, "var_decl");

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.addExpression(item);

                this.increasePointer();
                this.var_decl();
            } else {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
            }
        } else if (item.getToken().equals(Token.TK_APR)) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_FPR)) {
                this.addExpression(item);

                this.increasePointer();
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FDI)) {
                    this.addExpression(item);
                    this.saveExpression(expression, "var_decl_array");

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
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    item = this.getItem();

                    if (item.getToken().equals(Token.TK_FDI)) {
                        this.addExpression(item);
                        this.saveExpression(expression, "var_decl_equal");

                        this.increasePointer();
                        this.continueAnaliser();
                    } else if (item.getToken().equals(Token.TK_VIR)) {
                        this.addExpression(item);
                        this.saveExpression(expression, "var_decl_equal");

                        this.increasePointer();
                        item = this.getItem();

                        if (item.getToken().equals(Token.TK_ID)) {
                            this.addExpression(item);

                            this.increasePointer();
                            this.var_decl();
                        } else {
                            this.error.add("Esperava receber um ID na linha: " + item.getLine());
                            this.errorSkype();
                        }
                    } else {
                        this.error.add("Esperava receber ; na linha: " + item.getLine());
                        this.errorSkype();
                    }
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                this.errorSkype();
            }
        } else {
            this.error.add("Esperava receber ; [ = , na linha: " + item.getLine());
            this.errorSkype();
        }
    }

    public void fun_decl() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_AP)) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (!this.params())
                return;

            item = this.getItem();

            if (!item.getToken().equals(Token.TK_FP)) {
                this.error.add("Esperava receber )  na linha: " + item.getLine());
                this.errorSkype();
                return;
            }
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_FDI)) {
                this.addExpression(item);
                this.saveExpression(expression, "fun_decl_prototype");

                this.increasePointer();
                this.continueAnaliser();
                return;
            }
            item = this.getItem();

            if (this.isComStmtFirst(item)) {
                this.addExpression(item);
                this.saveExpression(expression, "fun_decl");

                this.com_stmt(false);
            } else {
                this.error.add("Esperava receber {  na linha: " + item.getLine());
                this.errorSkype();
            }
        } else {
            this.error.add("Esperava receber (  na linha: " + item.getLine());
            this.errorSkype();
        }
    }

    public boolean local_decl_1() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_FDI)) {
            this.addExpression(item);
            this.saveExpression(expression, "var_decl");

            this.increasePointer();
            return this.local_decls();
        } else if (item.getToken().equals(Token.TK_VIR)) {
            this.addExpression(item);
            this.saveExpression(expression, "var_decl");

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.addExpression(item);
                this.increasePointer();
                return this.local_decl_1();
            } else {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_APR)) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_FPR)) {
                this.addExpression(item);

                this.increasePointer();
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FDI)) {
                    this.addExpression(item);
                    this.saveExpression(expression, "var_decl_array");

                    this.increasePointer();
                    return this.local_decls();
                } else {
                    this.error.add("Esperava receber ; na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                this.error.add("Esperava receber ] na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_IG)) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    item = this.getItem();

                    if (item.getToken().equals(Token.TK_FDI)) {
                        this.addExpression(item);
                        this.saveExpression(expression, "var_decl_equal");

                        this.increasePointer();
                        return this.local_decls();
                    } else if (item.getToken().equals(Token.TK_VIR)) {
                        this.addExpression(item);
                        this.saveExpression(expression, "var_decl_equal");

                        this.increasePointer();
                        item = this.getItem();

                        if (item.getToken().equals(Token.TK_ID)) {
                            this.addExpression(item);
                            this.increasePointer();
                            return this.local_decl_1();
                        } else {
                            this.error.add("Esperava receber um ID na linha: " + item.getLine());
                            this.errorSkype();
                            return false;
                        }
                    } else {
                        this.error.add("Esperava receber ; na linha: " + item.getLine());
                        this.errorSkype();
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            this.error.add("Esperava receber ;  [ = na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
    }

    public boolean local_decls() {
        this.clearExpression();
        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.addExpression(item);
                this.increasePointer();

                if (this.local_decl_1()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean com_stmt(boolean get) {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_ABC)) {
            this.increasePointer();

            if (this.content()) {
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FCH)) {
                    this.increasePointer();

                    if (get) {
                        return true;
                    } else {
                        this.continueAnaliser();
                        return true;
                    }
                } else {
                    this.error.add("Esperava receber } na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                return false;
            }

        } else {
            this.error.add("Esperava receber {  na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
    }

    public boolean content() {
        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            if (this.local_decls())
                return this.content();
            else
                return false;
        } else if (this.isStmtFirst(item)) {
            if (this.stmt())
                return this.content();
            else
                return false;
        } else {
            return true;
        }
    }

    public boolean exp_stmt() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_FDI)) {
            this.increasePointer();
            return true;
        }

        if (isExpFirst(item)) {
            if (this.exp()) {
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FDI)) {
                    this.addExpression(item);
                    this.saveExpression(expression, "exp_stmt");

                    this.increasePointer();
                    return true;
                } else {
                    this.error.add("Esperava receber ; na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean if_stmt_1() {
        Lexema item = this.getItem();

        if (item.getLexema().equals("else")) {
            this.increasePointer();
            item = this.getItem();

            if (isStmtFirst(item)) {
                return this.stmt();
            } else {
                this.error.add("Esperava receber um statment na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean if_stmt() {
        this.clearExpression();

        Lexema item = this.getItem();

        if (item.getLexema().equals("if")) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_AP)) {
                this.addExpression(item);

                this.increasePointer();
                item = this.getItem();

                if (isExpFirst(item)) {
                    if (this.exp()) {
                        item = this.getItem();

                        if (item.getToken().equals(Token.TK_FP)) {
                            this.addExpression(item);
                            this.saveExpression(expression, "if_stmt");

                            this.increasePointer();
                            item = this.getItem();

                            if (this.isStmtFirst(item)) {
                                if (this.stmt()) {
                                    if (this.if_stmt_1()) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            } else {
                                this.error.add("Esperava receber um statment na linha: " + item.getLine());
                                this.errorSkype();
                                return false;
                            }
                        } else {
                            this.error.add("Esperava receber ) na linha: " + item.getLine());
                            this.errorSkype();
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                this.error.add("Esperava receber ( na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean return_stmt_1() {
        Lexema item = this.getItem();

        if (item.getLexema().equals(";")) {
            this.increasePointer();
            return true;
        } else if (this.isExpFirst(item)) {
            if (this.exp()) {
                item = this.getItem();

                if (item.getLexema().equals(";")) {
                    this.increasePointer();
                    return true;
                } else {
                    this.error.add("Esperava receber um ; na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                return false;
            }
        } else {
            this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
    }

    public boolean return_stmt() {
        Lexema item = this.getItem();

        if (item.getLexema().equals("return")) {
            this.increasePointer();
            item = this.getItem();

            return this.return_stmt_1();
        } else {
            return false;
        }
    }

    public boolean stmt() {
        Lexema item = this.getItem();

        if (isExpFirst(item) || item.getToken().equals(Token.TK_FDI)) {
            return this.exp_stmt();
        } else if (item.getToken().equals(Token.TK_ABC)) {
            return this.com_stmt(true);
        } else if (item.getLexema().equals("if")) {
            return this.if_stmt();
        } else if (item.getLexema().equals("while")) {
            return this.while_stmt();
        } else if (item.getLexema().equals("return")) {
            return this.return_stmt();
        } else if (item.getLexema().equals("break")) {
            return this.break_stmt();
        } else if (item.getLexema().equals("for")) {
            return this.for_stmt();
        } else if (item.getLexema().equals("do")) {
            return this.do_stmt();
        } else {
            return false;
        }
    }

    public boolean do_stmt() {
        this.clearExpression();
        Lexema item = this.getItem();

        if (item.getLexema().equals("do")) {
            this.addExpression(item);
            this.saveExpression(expression, "do_stmt");

            this.increasePointer();
            item = this.getItem();

            if (isStmtFirst(item)) {
                if (this.stmt()) {
                    item = this.getItem();

                    if (item.getLexema().equals("while")) {
                        this.clearExpression();
                        this.addExpression(item);

                        this.increasePointer();
                        item = this.getItem();

                        if (!item.getLexema().equals("(")) {
                            this.error.add("Esperava receber ( na linha: " + item.getLine());
                            this.errorSkype();
                            return false;
                        }

                        this.addExpression(item);
                        this.increasePointer();
                        item = this.getItem();

                        if (!isExpFirst(item)) {
                            this.error.add("Esperava receber ID ! + int float... na linha: " + item.getLine());
                            this.errorSkype();
                            return false;
                        }

                        if (this.exp()) {
                            item = this.getItem();

                            if (!item.getLexema().equals(")")) {
                                this.error.add("Esperava receber ) na linha: " + item.getLine());
                                this.errorSkype();
                                return false;
                            }
                            this.addExpression(item);
                            this.saveExpression(expression, "do_while_stmt");

                            this.increasePointer();
                            item = this.getItem();

                            if (!item.getLexema().equals(";")) {
                                this.error.add("Esperava receber ; na linha: " + item.getLine());
                                this.errorSkype();
                                return false;
                            }

                            this.increasePointer();
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        this.error.add("Esperava receber while na linha: " + item.getLine());
                        this.errorSkype();
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um statment na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean for_stmt() {
        this.clearExpression();
        Lexema item = this.getItem();

        if (!item.getLexema().equals("for")) {
            return false;
        }
        this.addExpression(item);

        this.increasePointer();
        item = this.getItem();

        if (!item.getToken().equals(Token.TK_AP)) {
            this.error.add("Esperava receber ( na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
        this.addExpression(item);
        this.increasePointer();

        if (!this.for_stmt_1()) {
            return false;
        }
        item = this.getItem();

        if (!item.getToken().equals(Token.TK_FDI)) {
            this.error.add("Esperava receber ; na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
        this.addExpression(item);
        this.increasePointer();
        item = this.getItem();

        if (!isExpFirst(item)) {
            this.error.add("Esperava receber ID ! + int float... na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }

        if (!this.exp()) {
            return false;
        }
        item = this.getItem();

        if (!item.getToken().equals(Token.TK_FDI)) {
            this.error.add("Esperava receber ; na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
        this.addExpression(item);
        this.increasePointer();
        item = this.getItem();

        if (!isExpFirst(item)) {
            this.error.add("Esperava receber ID ! + int float... na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }

        if (!this.exp()) {
            return false;
        }

        item = this.getItem();

        if (!item.getToken().equals(Token.TK_FP)) {
            this.error.add("Esperava receber ) na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }
        this.addExpression(item);
        this.saveExpression(expression, "for_stmt");

        this.increasePointer();
        item = this.getItem();

        if (!isStmtFirst(item)) {
            this.error.add("Esperava receber um statment na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }

        return this.stmt();
    }

    public boolean for_stmt_1() {
        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (!item.getToken().equals(Token.TK_ID)) {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
            this.addExpression(item);
            this.increasePointer();

            item = this.getItem();

            if (!item.getToken().equals(Token.TK_IG)) {
                this.error.add("Esperava receber um = na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (!isExpFirst(item)) {
                this.error.add("Esperava receber um ID + ! int, float... na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }

            return this.exp();
        }

        if (!isExpFirst(item)) {
            this.error.add("Esperava receber um ID + ! int, float... na linha: " + item.getLine());
            this.errorSkype();
            return false;
        }

        return this.exp();
    }

    public boolean break_stmt() {
        Lexema item = this.getItem();

        if (item.getToken().equals("break")) {
            this.increasePointer();
            item = this.getItem();

            if (item.getLexema().equals(";")) {
                this.increasePointer();
                return true;
            } else {
                this.error.add("Esperava receber ; na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean while_stmt() {
        this.clearExpression();
        Lexema item = this.getItem();

        if (item.getLexema().equals("while")) {
            this.addExpression(item);

            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_AP)) {
                this.addExpression(item);

                this.increasePointer();
                item = this.getItem();

                if (isExpFirst(item)) {
                    if (this.exp()) {
                        item = this.getItem();

                        if (item.getToken().equals(Token.TK_FP)) {
                            this.addExpression(item);
                            this.saveExpression(expression, "while_stmt");

                            this.increasePointer();
                            item = this.getItem();

                            if (isStmtFirst(item)) {
                                return this.stmt();
                            } else {
                                this.error.add("Esperava receber um statment na linha: " + item.getLine());
                                this.errorSkype();
                                return false;
                            }
                        } else {
                            this.error.add("Esperava receber ) na linha: " + item.getLine());
                            this.errorSkype();
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                this.error.add("Esperava receber ( " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean stmt_list() {
        Lexema item = this.getItem();

        if (isStmtFirst(item)) {
            if (this.stmt()) {
                return this.stmt_list();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean params() {
        Lexema item = this.getItem();

        if (item.getLexema().equals(Token.TK_VOID)) {
            this.addExpression(item);
            this.increasePointer();
            return true;
        }

        if (this.param()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean param() {
        Lexema item = this.getItem();

        if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_ID)) {
                this.addExpression(item);
                this.increasePointer();

                if (this.param_1()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean param_1() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_APR)) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (item.getToken().equals(Token.TK_FPR)) {
                this.addExpression(item);
                this.increasePointer();
                return this.param_2();
            } else {
                this.error.add("Esperava receber um ] na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_VIR)) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
                this.addExpression(item);
                this.increasePointer();
                item = this.getItem();

                if (item.getToken().equals(Token.TK_ID)) {
                    this.addExpression(item);
                    this.increasePointer();
                    return this.param_1();
                } else {
                    this.error.add("Esperava receber um ID na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                this.error.add("Esperava receber um float char int... na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean param_2() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_VIR)) {
            this.increasePointer();
            item = this.getItem();

            if (TYPE_SPEC_ARRAY.contains(item.getLexema())) {
                this.increasePointer();
                item = this.getItem();

                if (item.getToken().equals(Token.TK_ID)) {
                    this.increasePointer();
                    return this.param_1();
                } else {
                    this.error.add("Esperava receber um ID na linha: " + item.getLine());
                    this.errorSkype();
                    return false;
                }
            } else {
                this.error.add("Esperava receber um float char int... na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean exp() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_ID)) {
            this.addExpression(item);

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
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

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
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_NF) || item.getToken().equals(Token.TK_NI)
                || item.getToken().equals(Token.TK_CH) || item.getToken().equals(Token.TK_ST)) {
            this.addExpression(item);
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
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (isExpFirst(item)) {
                if (this.exp()) {
                    item = this.getItem();

                    if (item.getToken().equals(Token.TK_FPR)) {
                        this.addExpression(item);
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
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_PONT)) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (item.getLexema().equals("sizeof")) {
                this.addExpression(item);
                this.increasePointer();
                return true;
            } else {
                this.error.add("[exp_1] Esperava receber sizeof");
                this.errorSkype();
                return false;
            }
        } else if (item.getToken().equals(Token.TK_AP)) {
            this.addExpression(item);
            this.increasePointer();

            if (this.args()) {
                item = this.getItem();

                if (item.getToken().equals(Token.TK_FP)) {
                    this.addExpression(item);
                    this.increasePointer();
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
            this.addExpression(item);
            this.increasePointer();
            return true;
        } else if (item.getToken().equals(Token.TK_MEM)) {
            this.addExpression(item);
            this.increasePointer();
            return true;
        }

        return true;
    }

    public boolean exp_2() {
        Lexema item = this.getItem();

        if (item.getToken().equals(Token.TK_IG)) {
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

            if (this.isExpFirst(item)) {
                if (this.exp()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
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
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

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
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
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
            this.addExpression(item);
            this.increasePointer();
            item = this.getItem();

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
                this.error.add("Esperava receber um ID + - ! float char ou int na linha: " + item.getLine());
                this.errorSkype();
                return false;
            }
        } else {
            return true;
        }
    }

    // validations
    public boolean isKeyWord(Lexema item) {
        return "if".contains(item.getLexema())
                || "for".contains(item.getLexema())
                || "while".contains(item.getLexema())
                || "return".contains(item.getLexema())
                || "break".contains(item.getLexema());

    }

    public boolean isVardDeclFirst(Lexema item) {
        return ";[=,".contains(item.getLexema());
    }

    public boolean isFunDeclFirst(Lexema item) {
        return "(".contains(item.getLexema());
    }

    public boolean isComStmtFirst(Lexema item) {
        return "{".contains(item.getLexema());
    }

    public boolean isExpFirst(Lexema item) {
        return item.getToken().equals(Token.TK_ID) || item.getLexema().equals("+") || item.getLexema().equals("-")
                || item.getLexema().equals("!")
                || item.getToken().equals(Token.TK_NF) || item.getToken().equals(Token.TK_NI)
                || item.getToken().equals(Token.TK_CH)
                || item.getToken().equals(Token.TK_ST);
    }

    public boolean isStmtFirst(Lexema item) {
        return isExpFirst(item) || item.getToken().equals(Token.TK_FDI) || (item.getToken().equals(Token.TK_ABC))
                || item.getLexema().equals("if")
                || item.getLexema().equals("while")
                || item.getLexema().equals("return")
                || item.getLexema().equals("break")
                || item.getLexema().equals("do")
                || item.getLexema().equals("for");
    }
}
