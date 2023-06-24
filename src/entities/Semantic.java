package entities;

import java.util.ArrayList;

public class Semantic {
    private ArrayList<Lexema> signature;
    private String type;

    public Semantic(ArrayList<Lexema> signature, String type) {
        this.signature = signature;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public ArrayList<Lexema> getSignature() {
        return this.signature;
    }
}
