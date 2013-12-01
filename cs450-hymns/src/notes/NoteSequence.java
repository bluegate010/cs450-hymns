package notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import editdistance.EditDistanceCalculator;
import fft.WAV_FFT;

public class NoteSequence {

	private ArrayList<String> sequence;

	private static EditDistanceCalculator comparator;
	static {
		comparator = new EditDistanceCalculator();
	}

	public static NoteSequence generateSequenceForWavFile(String wavFilePath) {
		NoteTrainer trainer = NoteTrainer.getTrainer();
		NoteSequence sequence = new NoteSequence();
		double[][] ffts = WAV_FFT.getFFTs(wavFilePath);
		for (double[] fft : ffts) {
			String fftLabel = trainer.classifyFFT(fft);
			sequence.addToSequence(fftLabel);
		}

		return sequence;
	}

	public NoteSequence() {
		this.sequence = new ArrayList<>();
	}

	public List<String> getSequence() {
		return Collections.unmodifiableList(sequence);
	}

	/**
	 * Adds a string to the sequence, but only if it is different from the last
	 * item in the sequence. No repetition is allowed.
	 * 
	 * @param element
	 *            - new string to add to sequence
	 * @return boolean value indicating whether the element was added.
	 */
	private boolean addToSequence(String element) {
		if (!sequence.isEmpty()
				&& sequence.get(sequence.size() - 1).equals(element)) {
			return false;
		}

		sequence.add(element);
		return true;
	}

	public int distanceTo(NoteSequence other) {
		return comparator.computeAlignment(this.sequence, other.sequence);
	}
}
