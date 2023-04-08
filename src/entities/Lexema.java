package entities;

public class Lexema {
    private String token;
    private String lexema;

    public Lexema(String token, String lexema) {
        this.token = token;
        this.lexema = lexema;
    }

    public String getToken() {
        return this.token;
    }

    public String getLexema() {
        return this.lexema;
    }
}
