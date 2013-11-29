package test;

import com.musicg.wave.Wave;

public class WavRead {

	public static void main(String[] args) {
		String desktop = "/Users/jandersen/Desktop";
		String filename = desktop + "/cleaned/out.wav";

		// create a wave object
		Wave wave = new Wave(filename);

		// print the wave header and info
		System.out.println(wave);

		double[] amplitudes = wave.getNormalizedAmplitudes();

		System.out.println(amplitudes.length);

		// 8192 = nearest power of 2 to a quarter-second sample size
	}
}
