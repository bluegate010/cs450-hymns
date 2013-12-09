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

	private NNLearner learner;

	private static Map<String, NoteTrainer> trainers = new HashMap<>();

	public static NoteTrainer getTrainer(String trainingDir) {
		if (!trainers.containsKey(trainingDir)) {
			NoteTrainer trainer = new NoteTrainer(trainingDir);
			trainers.put(trainingDir, trainer);
		}

		return trainers.get(trainingDir);
	}

	public NoteTrainer(String dirPath) {
		loadTrainingData(dirPath);
	}

	public NoteTrainer() {
		learner = new NNLearner();
	}

	private void loadTrainingData(String dirPath) {
		File[] wavFiles = FileUtils.getWavFiles(dirPath);
		List<ClassifiedFFT> ffts = new ArrayList<>();

		if (wavFiles != null) {
			for (File wavFile : wavFiles) {
				String noteName = wavFile.getName().toUpperCase();

				int lastDotIndex = noteName.lastIndexOf(".");
				if (lastDotIndex > -1) {
					noteName = noteName.substring(0, lastDotIndex);
				}

				ffts.addAll(loadFFTs(wavFile, noteName));
			}
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

	public boolean saveTrainer(String outputFilePath) {
		return learner.saveLearner(outputFilePath);
	}

	public boolean loadTrainerFromFile(String inputFilePath) {
		return learner.loadLearnerFromFile(inputFilePath);
	}
}
