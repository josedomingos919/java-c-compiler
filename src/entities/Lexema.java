package entities;

public class Lexema {
    private String token;
    private String lexema;
    private int line;
    private int index;
    private int scope;

    public Lexema(String token, String lexema, int line, int scope) {
        this.token = token;
        this.lexema = lexema;
        this.line = line;
        this.scope = scope;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return this.line;
    }

    public int getScope() {
        return this.scope;
    }

    public String getToken() {
        return this.token;
    }

    public String getLexema() {
        return this.lexema;
    }
}
