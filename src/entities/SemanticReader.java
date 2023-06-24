package entities;

import java.util.ArrayList;

public class SemanticReader {
    private ArrayList<Semantic> semanticTable;
    private ArrayList<String> erros;

    public SemanticReader(ArrayList<Semantic> semanticTable) {
        this.semanticTable = semanticTable;
        this.erros = new ArrayList<>();

        this.checkDeclarationOfUsedVars();
        this.checkDoubleVarDeclaration();
    }

    public ArrayList<String> getErros() {
        return this.erros;
    }

    boolean checkIdDeclaration(int scope, int endIndex, Lexema value) {
        for (int i = 0; i < endIndex; i++) {
            Semantic item = semanticTable.get(i);

            // filter var and function declaration
            if (item.getType().equals("var_decl") ||
                    item.getType().equals("fun_decl") ||
                    item.getType().equals("var_decl_equal") ||
                    item.getType().equals("fun_decl_prototype")) {
                ArrayList<Lexema> lexemas = item.getSignature();

                // check top scope
                if (lexemas.get(0).getScope() >= scope || lexemas.get(0).getScope() == 0) {

                    // check if ID exist in position 0 or 1 of declaration
                    if (lexemas.get(0).getToken().equals(Token.TK_ID) &&
                            lexemas.get(0).getLexema().equals(value.getLexema()) ||
                            lexemas.get(1).getToken().equals(Token.TK_ID) &&
                                    lexemas.get(1).getLexema().equals(value.getLexema())) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    // a) Verificar se a variável usada foi declarada;
    public void checkDeclarationOfUsedVars() {
        int i = 0;
        int topScope = 0;

        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("exp_stmt")) {

                int j = 0;
                for (Lexema item : expression.getSignature()) {
                    if (item.getToken().equals(Token.TK_ID)) {
                        if (!checkIdDeclaration(topScope, i, item)) {
                            Lexema nextLexema = expression.getSignature().get(j + 1);
                            String IDName = "";

                            if (nextLexema.getToken().equals(Token.TK_AP)) {
                                IDName = "Funcao";
                            } else {
                                IDName = "Variavel";
                            }

                            this.erros.add(
                                    IDName + " " + item.getLexema() + " nao foi declarada na linha: " + item.getLine());

                        }
                    }

                    j++;
                }
            }

            i++;
        }
    }

    // c) Uma variável não deve ser declarada duas vezes no mesmo escopo;
    public void checkDoubleVarDeclaration() {
        ArrayList<Semantic> vars = this.getVarDecls();

        // filter var declaration
        for (Semantic expression : vars) {
            int count = 0;
            Lexema lexemaID = getVarID(expression);
            int scope = expression.getSignature().get(0).getScope();

            for (Semantic expression1 : vars) {
                Lexema lexemaID1 = getVarID(expression1);
                int scope1 = expression1.getSignature().get(0).getScope();

                if (scope1 == scope && lexemaID.getLexema().equals(lexemaID1.getLexema())) {
                    if (count > 0) {
                        String errorMessage = "Declaracao da variavel " + lexemaID.getLexema() + " duplicada na linha: "
                                + lexemaID1.getLine();

                        if (!erros.contains(errorMessage))
                            this.erros.add(errorMessage);
                    }

                    count++;
                }
            }
        }
    }

    public Lexema getVarID(Semantic expression) {
        return expression.getSignature().get(expression.getSignature().get(0).getToken().equals(Token.TK_ID) ? 0 : 1);
    }

    public ArrayList<Semantic> getVarDecls() {
        ArrayList<Semantic> vars = new ArrayList<>();

        // filter var declaration
        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("var_decl") ||
                    expression.getType().equals("var_decl_array") ||
                    expression.getType().equals("var_decl_equal")

            ) {
                vars.add(expression);
            }
        }

        return vars;
    }
}
