package entities;

import java.util.ArrayList;

public class SemanticReader {

    private ArrayList<Semantic> semanticTable;
    private ArrayList<String> erros;

    public SemanticReader(ArrayList<Semantic> semanticTable) {
        this.semanticTable = semanticTable;
        this.erros = new ArrayList<>();

        this.checkDeclarationOfUsedVars();
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
}
