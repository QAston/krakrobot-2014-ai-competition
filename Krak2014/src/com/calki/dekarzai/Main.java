package com.calki.dekarzai;

import com.calki.util.SoundUtil;

public class Main {
	
	public static void main(String[] args) {
		try {
			new DekarzAI().run();
		}
		// obsluga bledow
		catch(Exception ex)
		{
			SoundUtil.podwojnyDzwiek();
			throw ex;
		}
	}
}
