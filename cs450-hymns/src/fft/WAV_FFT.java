package fft;

import com.musicg.wave.Wave;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class WAV_FFT {

	public static final int DEFAULT_FFT_BIN_COUNT = 128;
	public static final double DEFAULT_SECONDS_PER_FFT = 0.25;
	public static final double DEFAULT_SECONDS_PER_SHIFT = DEFAULT_SECONDS_PER_FFT;

	public static double[][] getFFTs(String wavPath) {
		return getFFTs(wavPath, DEFAULT_FFT_BIN_COUNT, DEFAULT_SECONDS_PER_FFT,
				DEFAULT_SECONDS_PER_SHIFT);
	}

	public static double[][] getFFTs(String wavPath, int fftBinCount,
			double secondsPerFFT, double secondsPerShift) {
		Wave wave = new Wave(wavPath);
		double[] amplitudes = wave.getNormalizedAmplitudes();
		int samplesPerSecond = wave.getWaveHeader().getSampleRate();

		// Calculate sample numbers
		int samplesPerFFT = (int) (secondsPerFFT * samplesPerSecond);
		int samplesPerShift = (int) (secondsPerShift * samplesPerSecond);

		samplesPerFFT = roundUpToMultiple(samplesPerFFT, fftBinCount);

		// Calculate the number of FFTs there will be out of this WAV file
		int numFFTs = (int) Math
				.ceil(((double) (amplitudes.length - samplesPerFFT))
						/ samplesPerShift);

		double[][] ffts = new double[numFFTs][];
		double[] data = new double[samplesPerFFT];

		for (int fftNum = 0; fftNum < numFFTs; fftNum++) {
			int startSample = fftNum * samplesPerShift;
			System.arraycopy(amplitudes, startSample, data, 0, samplesPerFFT);
			ffts[fftNum] = getFFTMagnitude(data, fftBinCount);
		}

		return ffts;
	}

	private static double[] getFFTMagnitude(double[] data, int binCount) {
		if (data.length % binCount != 0) {
			throw new IllegalArgumentException("fft input length ("
					+ data.length + ") must be multiple of bin count ("
					+ binCount + ")");
		}

		DoubleFFT_1D fft = new DoubleFFT_1D(binCount);

		double[] magnitudeSum = new double[binCount];
		double[] fftData = new double[binCount * 2];
		int sumCount = 0;

		for (int startIndex = 0; startIndex + binCount <= data.length; startIndex += binCount) {
			realToComplex(data, fftData, startIndex, binCount);
			fft.complexForward(fftData);
			incrementWithMagnitude(fftData, magnitudeSum);
			sumCount++;
		}

		// Calculate average values for each bin.
		for (int i = 0; i < binCount; i++) {
			magnitudeSum[i] /= sumCount;
		}

		return magnitudeSum;
	}

	private static void realToComplex(double[] input, double[] output,
			int start, int length) {
		for (int i = 0; i < length; i++) {
			output[i * 2] = input[start + i];
			output[i * 2 + 1] = 0;
		}
	}

	private static void incrementWithMagnitude(double[] fftOutput,
			double[] magnitude) {
		for (int i = 0; i < fftOutput.length; i += 2) {
			magnitude[i / 2] += Math.sqrt(Math.pow(fftOutput[i], 2)
					+ Math.pow(fftOutput[i + 1], 2));
		}
	}

	// From http://stackoverflow.com/a/3407254
	private static int roundUpToMultiple(int value, int multiple) {
		if (multiple == 0) {
			return value;
		}

		int remainder = value % multiple;
		if (remainder == 0) {
			return value;
		}

		return value + multiple - remainder;
	}
}
