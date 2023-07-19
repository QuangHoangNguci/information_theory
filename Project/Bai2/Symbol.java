package Bai2;

import java.util.*;

public class Symbol {
    public char c;
    public int frequency;
    public String code;

    public Symbol left;
    public Symbol right; // Các node con trái, phải

    public Symbol(char c, int frequency) {
        this.c = c;
        this.frequency = frequency;
        this.code = "";
    }
}

