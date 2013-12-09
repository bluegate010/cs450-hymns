package notes;

import java.util.ArrayList;
import java.util.Collection;

import editdistance.EditDistanceCalculator;
import fft.WAV_FFT;

/**
 * Represents a sequence of notes. Repetition is not allowed; for all x from 0
 * to sequence.size() - 2, sequence.get(x).equals(sequence.get(x+1)) is false.
 * <p>
 * Notes can only be added to the end of the sequence. Removal or subsequent
 * modification is not permitted.
 * 
 * @author jandersen
 */
public class NoteSequence extends ArrayList<String> {

	private static final Character NOTE_DELIMITER = ',';

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -5179136190035832414L;

	private static final EditDistanceCalculator comparator = new EditDistanceCalculator();

	public static NoteSequence generateSequenceForWavFile(String wavFilePath,
			NoteTrainer trainer) {
		NoteSequence sequence = new NoteSequence();
		double[][] ffts = WAV_FFT.getFFTs(wavFilePath);
		for (double[] fft : ffts) {
			sequence.add(trainer.classifyFFT(fft));
		}

		return sequence;
	}

	public static NoteSequence fromString(String string) {
		String[] notes = string.split(Character.toString(NOTE_DELIMITER));
		NoteSequence sequence = new NoteSequence();

		for (String note : notes) {
			sequence.add(note);
		}

		return sequence;
	}

	public NoteSequence() {
		super();
	}

	public int distanceTo(NoteSequence other) {
		return comparator.computeAlignment(this, other);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (String note : this) {
			sb.append(NOTE_DELIMITER + note);
		}

		// Trim off leading comma
		return sb.length() > 0 ? sb.substring(1) : "";
	}

	/**
	 * Only adds to the sequence if the end element is not equal to the given
	 * element. Does not allow null values.
	 */
	@Override
	public boolean add(String note) {
		if (note == null || this.endElementEquals(note)) {
			return false;
		}

		super.add(note);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends String> notes) {
		boolean changed = false;
		for (String note : notes) {
			changed |= add(note);
		}

		return changed;
	}

	private boolean endElementEquals(String note) {
		return !this.isEmpty() && this.get(this.size() - 1).equals(note);
	}

	// ************************************************************************
	// Unsupported Operations

	@Override
	public void add(int index, String note) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> notes) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String set(int index, String note) {
		throw new UnsupportedOperationException();
	}
}
