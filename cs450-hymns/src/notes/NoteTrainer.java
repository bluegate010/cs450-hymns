package notes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nearestneighbor.ClassifiedFFT;
import nearestneighbor.NNLearner;
import utils.FileUtils;
import fft.WAV_FFT;

public class NoteTrainer {

	public static final String DEFAULT_TRAINING_DIR = "audio/training/";

	private NNLearner learner;

	private static Map<String, NoteTrainer> trainers = new HashMap<>();

	public static NoteTrainer getTrainer() {
		return getTrainer(DEFAULT_TRAINING_DIR);
	}

	public static NoteTrainer getTrainer(String trainingDir) {
		if (!trainers.containsKey(trainingDir)) {
			NoteTrainer trainer = new NoteTrainer(trainingDir);
			trainers.put(trainingDir, trainer);
		}

		return trainers.get(trainingDir);
	}

	private NoteTrainer(String dirPath) {
		loadTrainingData(dirPath);
	}

	private void loadTrainingData(String dirPath) {
		File[] wavFiles = FileUtils.getWavFiles(dirPath);
		List<ClassifiedFFT> ffts = new ArrayList<>();

		for (File wavFile : wavFiles) {
			String noteName = wavFile.getName().toUpperCase();
			ffts.addAll(loadFFTs(wavFile, noteName));
		}

		learner = new NNLearner(ffts);
	}

	public String classifyFFT(double[] fftData) {
		return learner.classify(fftData);
	}

	private static List<ClassifiedFFT> loadFFTs(File wavFile, String noteName) {
		double[][] ffts = WAV_FFT.getFFTs(wavFile.getAbsolutePath());
		List<ClassifiedFFT> classifiedFFTs = new ArrayList<>();

		for (double[] fft : ffts) {
			classifiedFFTs.add(new ClassifiedFFT(noteName, fft));
		}

		return classifiedFFTs;
	}

}
