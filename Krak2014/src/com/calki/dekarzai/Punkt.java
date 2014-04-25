package com.calki.dekarzai;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public class Punkt {
    public int x;
    public int y;

    public Punkt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public double odlegloscDo(Punkt p)
    {
        return (Math.sqrt(Math.pow((x - p.x), 2) + Math.pow((y - p.y), 2)));
    }
}
