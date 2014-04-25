package com.calki.util;

import lejos.nxt.Sound;

public class SoundUtil {
	public static void podwojnyDzwiek() {
		Sound.setVolume(100);
		Sound.twoBeeps();
	}

	public static void dzwiek() {
		Sound.setVolume(50);
		Sound.beep();
	}
}
