package com.calki.dekarzai;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public enum RodzajPola {

    POLE_NIEODWIEDZONE(0),
    POLE_KOMIN(1),
    POLE_KRAWEDZ(2),
    POLE_ODWIEDZONE(3);
    private int value;

    RodzajPola(int val) {
        this.value = val;
    }
    
    public int toInt()
    {
        return this.value;
    }
}
