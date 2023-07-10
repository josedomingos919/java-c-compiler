package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SemanticReader {
    private ArrayList<Semantic> semanticTable;
    private ArrayList<String> erros;
    private final List<String> TYPE_SPEC_ARRAY = Arrays.asList("void", "float", "char", "int", "double");

    public SemanticReader(ArrayList<Semantic> semanticTable) {
        this.semanticTable = semanticTable;
        this.erros = new ArrayList<>();

        this.checkDeclarationOfUsedVars();
        this.checkDoubleVarDeclaration();
        this.checkVarsValues();
        this.checkMainDeclaration();
        this.checkBooleanExpression();
        this.checkFunctionParams();

        // this.checkPrintf();
        // this.checkSanf();
    }

    public ArrayList<String> getErros() {
        return this.erros;
    }

    boolean checkIdDeclaration(int startIndex, int endIndex, Lexema value) {
        for (int i = 0; i < endIndex; i++) {
            Semantic item = semanticTable.get(i);

            // filter var and function declaration
            if (item.getType().equals("var_decl") ||
                    item.getType().equals("fun_decl") ||
                    item.getType().equals("var_decl_equal") ||
                    item.getType().equals("fun_decl_prototype")) {

                ArrayList<Lexema> lexemas = item.getSignature();

                // check top scope
                if (i >= startIndex || lexemas.get(0).getScope() == 0) {

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

    Semantic getVarFunDec(int startIndex, int endIndex, Lexema id) {
        for (int i = 0; i < endIndex; i++) {
            Semantic expression = semanticTable.get(i);

            // verify scope
            if (i >= startIndex || expression.getSignature().get(0).getScope() == 0)
                // filter var and function declaration
                if (expression.getType().equals("var_decl") ||
                        expression.getType().equals("fun_decl") ||
                        expression.getType().equals("var_decl_equal") ||
                        expression.getType().equals("var_decl_array") ||
                        expression.getType().equals("fun_decl_prototype")) {

                    ArrayList<Lexema> signature = expression.getSignature();

                    // check if ID exist in position 0 or 1 of declaration
                    if (signature.get(0).getToken().equals(Token.TK_ID) &&
                            signature.get(0).getLexema().equals(id.getLexema())) {
                        return expression;
                    } else if (signature.get(1).getToken().equals(Token.TK_ID) &&
                            signature.get(1).getLexema().equals(id.getLexema())) {
                        return expression;
                    }
                }
        }

        return null;
    }

    // a) Verificar se a variável usada foi declarada;
    public void checkDeclarationOfUsedVars() {
        int i = 0;
        int topScope = 0;

        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("fun_decl"))
                topScope = i;

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

    public ArrayList<Semantic> getVarEqual() {
        ArrayList<Semantic> vars = new ArrayList<>();

        // filter var declaration
        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("var_decl_equal")) {
                vars.add(expression);
            }
        }

        return vars;
    }

    public Lexema getVarDataType(Semantic expression) {
        if (TYPE_SPEC_ARRAY.contains(expression.getSignature().get(0).getLexema()))
            return expression.getSignature().get(0);

        for (int i = expression.getIndex(); i > 0; i--) {
            if (TYPE_SPEC_ARRAY.contains(this.semanticTable.get(i).getSignature().get(0).getLexema()))
                return this.semanticTable.get(i).getSignature().get(0);
        }

        return null;
    }

    // b) Compatibilidade de tipos, ou seja, uma variável do tipo inteiro não deve
    // receber por exemplo um valor do tipo string;
    public void checkVarsValues() {
        int startIndex = 0;

        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("fun_decl"))
                startIndex = expression.getIndex();

            // var_decl_equal
            if (expression.getType().equals("var_decl_equal")) {
                Lexema varId = this.getVarID(expression);
                Lexema varType = this.getVarDataType(expression);
                Boolean canValidate = false;
                Boolean hasIgual = false;

                for (Lexema item : expression.getSignature()) {
                    // int
                    if (varType.getLexema().equals("int")) {
                        // vaild token
                        if (canValidate) {
                            if (item.getToken().equals(Token.TK_NI) ||
                                    item.getToken().equals(Token.TK_CH) ||
                                    item.getToken().equals(Token.TK_VIR) ||
                                    item.getToken().equals(Token.TK_FDI) ||
                                    item.getToken().equals(Token.TK_M) ||
                                    item.getToken().equals(Token.TK_MEN) ||
                                    item.getToken().equals(Token.TK_AST)  
                            ) {
                                canValidate = true;
                            } else if (item.getToken().equals(Token.TK_AP) || item.getToken().equals(Token.TK_APR)) {
                                canValidate = false;
                            } else if (item.getToken().equals(Token.TK_DIV)) {
                                this.erros.add("Operador " + item.getLexema()
                                        + " Nao pode ser usado na linha: "
                                        + item.getLine());
                                canValidate = false;
                            } else if (item.getToken().equals(Token.TK_ID)) {
                                Semantic idExpression = getVarFunDec(startIndex, expression.getIndex(), item);

                                if (idExpression == null) {
                                    this.erros.add("Variavel/Funcao " + item.getLexema() + " Nao declarado na linha: "
                                            + item.getLine());
                                    this.erros.add(
                                            "Tipo de dados da variavel/funcao " + varId.getLexema()
                                                    + " nao identificado na linha: "
                                                    + item.getLine());
                                    canValidate = false;
                                } else {
                                    Lexema idToken = getVarID(idExpression);
                                    Lexema idType = getVarDataType(idExpression);

                                    if (!idType.getLexema().equals("int")) {
                                        this.erros.add(
                                                "Tipo de dados da funcao/variavel " + idToken.getLexema()
                                                        + " invalido na linha: "
                                                        + item.getLine());
                                        canValidate = false;
                                    }
                                }
                            } else {
                                this.erros.add(
                                        "Valor invalido para variavel " + varId.getLexema() + " na linha: "
                                                + item.getLine());
                                canValidate = false;
                            }
                        } else if (item.getToken().equals(Token.TK_FP) || item.getToken().equals(Token.TK_FPR)) {
                            canValidate = true;
                        } else if (item.getToken().equals(Token.TK_ID) && hasIgual && !canValidate) {
                            Semantic idExpression = getVarFunDec(startIndex, expression.getIndex(), item);

                            if (idExpression == null) {
                                this.erros.add(
                                        "Variavel/funcao " + item.getLexema() + " nao declarada na linha: "
                                                + item.getLine());
                            }
                        }
                    }

                    // float, double
                    if (varType.getLexema().equals("float") || varType.getLexema().equals("double")) {
                        // vaild token
                        if (canValidate) {
                            if (item.getToken().equals(Token.TK_NF) ||
                                    item.getToken().equals(Token.TK_NI) ||
                                    item.getToken().equals(Token.TK_VIR) ||
                                    item.getToken().equals(Token.TK_FDI) ||
                                    item.getToken().equals(Token.TK_M) ||
                                    item.getToken().equals(Token.TK_MEN) ||
                                    item.getToken().equals(Token.TK_AST) ||
                                    item.getToken().equals(Token.TK_DIV)

                            ) {
                                canValidate = true;
                            } else if (item.getToken().equals(Token.TK_AP) || item.getToken().equals(Token.TK_APR)) {
                                canValidate = false;
                            } else if (item.getToken().equals(Token.TK_ID)) {
                                Semantic idExpression = getVarFunDec(startIndex, expression.getIndex(), item);

                                if (idExpression == null) {
                                    this.erros.add("Variavel/Funcao " + item.getLexema() + " Nao declarado na linha: "
                                            + item.getLine());
                                    this.erros.add(
                                            "Tipo de dados da variavel/funcao " + varId.getLexema()
                                                    + " nao identificado na linha: "
                                                    + item.getLine());
                                    canValidate = false;
                                } else {
                                    Lexema idToken = getVarID(idExpression);
                                    Lexema idType = getVarDataType(idExpression);

                                    if (!idType.getLexema().equals("float") &&
                                            !idType.getLexema().equals("double") &&
                                            !idType.getLexema().equals("int")) {
                                        this.erros.add(
                                                "Tipo de dados da funcao/variavel " + idToken.getLexema()
                                                        + " invalido na linha: "
                                                        + item.getLine());
                                        canValidate = false;
                                    }
                                }
                            } else {
                                this.erros.add(
                                        "Valor invalido para variavel " + varId.getLexema() + " na linha: "
                                                + item.getLine());
                                canValidate = false;
                            }
                        } else if (item.getToken().equals(Token.TK_FP) || item.getToken().equals(Token.TK_FPR)) {
                            canValidate = true;
                        } else if (item.getToken().equals(Token.TK_ID) && hasIgual && !canValidate) {
                            Semantic idExpression = getVarFunDec(startIndex, expression.getIndex(), item);

                            if (idExpression == null) {
                                this.erros.add(
                                        "Variavel/funcao " + item.getLexema() + " nao declarada na linha: "
                                                + item.getLine());
                            }
                        }
                    }

                    // char
                    if (varType.getLexema().equals("char")) {
                        // vaild token
                        if (canValidate) {
                            if (item.getToken().equals(Token.TK_CH)) {
                                canValidate = false;
                            } else if (item.getToken().equals(Token.TK_ID)) {
                                canValidate = false;
                                Semantic idExpression = getVarFunDec(startIndex, expression.getIndex(), item);

                                if (idExpression == null) {
                                    this.erros.add("Variavel/Funcao " + item.getLexema() + " Nao declarado na linha: "
                                            + item.getLine());
                                    this.erros.add(
                                            "Tipo de dados da variavel/funcao " + varId.getLexema()
                                                    + " nao identificado na linha: "
                                                    + item.getLine());

                                } else {
                                    Lexema idToken = getVarID(idExpression);
                                    Lexema idType = getVarDataType(idExpression);

                                    if (!idType.getLexema().equals("char")) {
                                        this.erros.add(
                                                "Tipo de dados da funcao/variavel " + idToken.getLexema()
                                                        + " invalido na linha: "
                                                        + item.getLine());
                                    }
                                }
                            } else {
                                this.erros.add(
                                        "Valor invalido para variavel " + varId.getLexema() + " na linha: "
                                                + item.getLine());
                                canValidate = false;
                            }
                        } else if (item.getToken().equals(Token.TK_VIR) ||
                                item.getToken().equals(Token.TK_FDI)) {
                            canValidate = false;
                        } else if (hasIgual && !canValidate) {
                            this.erros.add(
                                    "Valor invalido para variavel " + varId.getLexema() + " na linha: "
                                            + item.getLine());
                            hasIgual = false;
                        }
                    }

                    // others
                    if (item.getToken().equals(Token.TK_IG)) {
                        canValidate = true;
                        hasIgual = true;
                    }
                }
            }
        }
    }

    // g) Fazer também a verificação do identificador principal (main) se foi bem
    // declarada;
    public void checkMainDeclaration() {
        boolean hasMain = false;

        for (Semantic expression : this.semanticTable) {
            // var_decl_equalfun_decl
            if (expression.getType().equals("fun_decl")) {
                Lexema varId = this.getVarID(expression);
                Lexema varType = this.getVarDataType(expression);

                if (varId.getLexema().equals("main")) {
                    hasMain = true;

                    // data type
                    if (!varType.getLexema().equals("int")) {
                        this.erros.add(
                                "O tipo de dados da funcao main precisa ser inteiro! na linha: "
                                        + varType.getLine());
                    }
                }
            }
        }

        if (!hasMain)
            this.erros.add("funcao main nao foi declarada!");
    }

    public void checkBooleanExpression() {
        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("if_stmt") ||
                    expression.getType().equals("while_stmt") ||
                    expression.getType().equals("do_while_stmt")) {
                boolean isBoolean = false;

                for (Lexema item : expression.getSignature()) {
                    if (Arrays.asList("==", ">=", "<=", "&&", "||", "!=", ">", "<").contains(item.getLexema()))
                        isBoolean = true;
                }

                if (!isBoolean && expression.getSignature().size() != 4) {
                    this.erros
                            .add("Expressao boleana faltando na linha: " + expression.getSignature().get(0).getLine());
                }
            }

            if (expression.getType().equals("for_stmt")) {
                boolean isBoolean = false;
                boolean canValidate = false;

                for (Lexema item : expression.getSignature()) {
                    if (item.getLexema().equals(";"))
                        canValidate = !canValidate;

                    if (canValidate
                            && Arrays.asList("==", ">=", "<=", "&&", "||", "!=", ">", "<").contains(item.getLexema()))
                        isBoolean = true;
                }

                if (!isBoolean) {
                    this.erros
                            .add("Expressao boleana faltando na linha: " + expression.getSignature().get(0).getLine());
                }
            }
        }
    }

    public void checkFunctionParams() {
        int startIndex = 0;

        for (Semantic expression : this.semanticTable) {
            if (expression.getType().equals("fun_decl"))
                startIndex = expression.getIndex();

            if (!expression.getType().equals("exp_stmt"))
                continue;

            for (int i = 0; i < expression.getSignature().size(); i++) {
                if (expression.getSignature().get(i).getToken().equals(Token.TK_ID)
                        && expression.getSignature().get(i + 1).getToken().equals(Token.TK_AP)) 
                {
                    ArrayList<Lexema> listParams = new ArrayList<>();
                    ArrayList<Lexema> listParamsTypes = new ArrayList<>();
                    Semantic sintaxiFun = getVarFunDec(startIndex, expression.getSignature().get(i).getIndex(), expression.getSignature().get(i));
                    int j;
                    int canValidate = 0;

                    if(sintaxiFun != null)
                        for (Lexema item : sintaxiFun.getSignature()) {
                            if(Arrays.asList("int", "float", "double", "char").contains(item.getLexema())) {
                                if(canValidate > 0) {
                                    listParamsTypes.add(item);
                                }else {
                                    canValidate++;
                                }
                            }
                        }
                  
                    for (j = i + 1; j < expression.getSignature().size()
                            && !expression.getSignature().get(j).getLexema().equals(")"); j++) {

                        if (!expression.getSignature().get(j).getLexema().equals("(")
                                && !expression.getSignature().get(j).getLexema().equals(")")
                                && !expression.getSignature().get(j).getLexema().equals(",")) {
                            listParams.add(expression.getSignature().get(j));
                        }
                    }

                    if(listParams.size() != listParamsTypes.size()) {
                        erros.add("Numero de parametros incompativeis na linha: " + expression.getSignature().get(i).getLine());
                    }else {
                        for(int y = 0; y < listParams.size() ; y++) {
                            if( listParamsTypes.get(y).getLexema().equals("int") && listParams.get(y).getToken() != Token.TK_NI && 
                                listParamsTypes.get(y).getLexema().equals("int") && listParams.get(y).getToken() != Token.TK_CH
                            ){
                                this.erros.add("Esperava receber um inteiro no parametro nº "+ (y + 1) +" da funcao "+ expression.getSignature().get(i).getLexema() +" na linha: " + expression.getSignature().get(j).getLine());
                            }

                            if(listParamsTypes.get(y).getLexema().equals("float") && listParams.get(y).getToken() != Token.TK_NF || 
                                listParamsTypes.get(y).getLexema().equals("float") && listParams.get(y).getToken() != Token.TK_NI
                            ){
                                this.erros.add("Esperava receber um float no prametro nº "+ (y + 1) +" da funcao "+ expression.getSignature().get(i).getLexema() +" na linha: " + expression.getSignature().get(j).getLine());
                            }

                            if(listParamsTypes.get(y).getLexema().equals("double") && listParams.get(y).getToken() != Token.TK_NF || 
                                listParamsTypes.get(y).getLexema().equals("double") && listParams.get(y).getToken() != Token.TK_NI
                            ){
                                this.erros.add("Esperava receber um double no prametro nº "+ (y + 1) +" da funcao "+ expression.getSignature().get(i).getLexema() +" na linha: " + expression.getSignature().get(j).getLine());
                            }
                            
                            if(listParamsTypes.get(y).getLexema().equals("char") && listParams.get(y).getToken() != Token.TK_CH){
                                this.erros.add("Esperava receber um char no prametro nº "+ (y + 1) +" da funcao "+ expression.getSignature().get(i).getLexema() +" na linha: " + expression.getSignature().get(j).getLine());
                            }
                        }
                    }

                    i = j;
                }
            }
        }
    }

    public void validateList() {
    }
}
