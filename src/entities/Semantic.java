package entities;

import java.util.ArrayList;

public class Semantic {
    private ArrayList<Lexema> signature;
    private String type;
    private int index = 0;
    private static int countIndex;

    public Semantic(ArrayList<Lexema> signature, String type) {
        this.signature = signature;
        this.type = type;

        this.index = Semantic.countIndex;
        Semantic.countIndex++;
    }

    public int getIndex() {
        return this.index;
    }

    public String getType() {
        return this.type;
    }

    public ArrayList<Lexema> getSignature() {
        return this.signature;
    }
}
