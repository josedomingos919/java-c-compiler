package entities;

public class Lexema {
    private String token;
    private String lexema;
    private int line;
    private int index;

    public Lexema(String token, String lexema, int line) {
        this.token = token;
        this.lexema = lexema;
        this.line = line;
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

    public String getToken() {
        return this.token;
    }

    public String getLexema() {
        return this.lexema;
    }
}
