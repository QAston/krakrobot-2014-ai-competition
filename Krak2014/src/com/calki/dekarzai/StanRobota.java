package com.calki.dekarzai;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public enum StanRobota {

    STAN_ANALIZA_PLANSZY(0),
    STAN_PRZEJAZD_PO_PLANSZY(1),
    STAN_KONIEC_PRZEJAZDU(2);
    
    private int value;

    StanRobota(int val) {
        this.value = val;
    }
    
    public int toInt()
    {
        return this.value;
    }
}
