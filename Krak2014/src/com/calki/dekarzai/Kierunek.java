package com.calki.dekarzai;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public enum Kierunek {

    KIERUNEK_GORA(0),
    KIERUNEK_DOL(1),
    KIERUNEK_LEWO(2),
    KIERUNEK_PRAWO(3);
    
    private int value;

    Kierunek(int val) {
        this.value = val;
    }
    
    public int toInt()
    {
        return this.value;
    }
    
    public Kierunek przeciwny()
    {
        switch (this)
        {
            case KIERUNEK_GORA:
                return Kierunek.KIERUNEK_DOL;
            case KIERUNEK_DOL:
                return Kierunek.KIERUNEK_GORA;
            case KIERUNEK_LEWO:
                return Kierunek.KIERUNEK_PRAWO;
            case KIERUNEK_PRAWO:
                return Kierunek.KIERUNEK_LEWO;
            default:
                return null;
        }
    }
    
    public Kierunek lewo()
    {
        switch (this)
        {
            case KIERUNEK_PRAWO:
                return Kierunek.KIERUNEK_GORA;
            case KIERUNEK_GORA:
                return Kierunek.KIERUNEK_LEWO;
            case KIERUNEK_LEWO:
                return Kierunek.KIERUNEK_DOL;
            case KIERUNEK_DOL:
                return Kierunek.KIERUNEK_PRAWO;
            default:
                return null;
        }
    }
    
    public Kierunek prawo()
    {
        switch (this)
        {
            case KIERUNEK_GORA:
                return Kierunek.KIERUNEK_PRAWO;
            case KIERUNEK_LEWO:
                return Kierunek.KIERUNEK_GORA;
            case KIERUNEK_DOL:
                return Kierunek.KIERUNEK_LEWO;
            case KIERUNEK_PRAWO:
                return Kierunek.KIERUNEK_DOL;
            default:
                return null;
        }
    }
    
	public Kierunek poPoleceniu(Polecenie polecenie)
	{
		switch(polecenie)
		{
			case JEDZ_PROSTO:
				return this;
			case JEDZ_DO_TYLU:
				return przeciwny();
			case JEDZ_W_LEWO:
				return lewo();
			case JEDZ_W_PRAWO:
				return prawo();
			default:
				return null;
		}
	}
    
   
}
