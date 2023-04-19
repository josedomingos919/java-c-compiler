package entities;

public class Lexema {
    private String token;
    private String lexema;
    private int line;

    public Lexema(String token, String lexema, int line) {
        this.token = token;
        this.lexema = lexema;
        this.line = line;
    }

    public void setLine(int line){
        this.line = line;
    }
    
    public int getLine(){
        return this.line;
    }
    
    public String getToken() {
        return this.token;
    }

    public String getLexema() {
        return this.lexema;
    }
}
