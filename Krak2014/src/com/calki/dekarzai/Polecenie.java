package com.calki.dekarzai;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public enum Polecenie {

    JEDZ_PROSTO(0),
    JEDZ_W_LEWO(1),
    JEDZ_W_PRAWO(2),
    JEDZ_DO_TYLU(3),
    ZAKONCZ_JAZDE(4);
    
    private int value;

    Polecenie(int val) {
        this.value = val;
    }
    
    public int toInt()
    {
        return this.value;
    }
}
