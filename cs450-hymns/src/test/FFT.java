package test;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class FFT {
	
	public static void main(String[] args) {
		double[] data = {
				8, 0,
				4, 0,
				8, 0,
				0, 0
		};
		
		DoubleFFT_1D d = new DoubleFFT_1D(4);
		d.complexForward(data, 0);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}
}
