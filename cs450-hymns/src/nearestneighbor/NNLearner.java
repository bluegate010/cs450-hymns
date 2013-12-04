package nearestneighbor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.eatthepath.jeospatial.SpatialPoint;
import com.eatthepath.jeospatial.util.SimpleSpatialPoint;
import com.eatthepath.jeospatial.vptree.VPTree;

public class NNLearner {

	public static final int DEFAULT_K = 10;

	private VPTree<ClassifiedFFT> pointDatabase;

	public NNLearner() {
		pointDatabase = new VPTree<ClassifiedFFT>();
	}

	public NNLearner(Collection<ClassifiedFFT> samples) {
		pointDatabase = new VPTree<ClassifiedFFT>(Math.max(1, samples.size()));
		pointDatabase.addAll(samples);
	}

	public String classify(double[] fftData) {
		return classify(fftData, DEFAULT_K);
	}

	public String classify(double[] fftData, int k) {
		SpatialPoint queryPoint = new SimpleSpatialPoint(fftData);
		List<ClassifiedFFT> nearest = pointDatabase.getNearestNeighbors(
				queryPoint, k);

		return getMostCommonLabel(nearest);
	}

	private String getMostCommonLabel(List<ClassifiedFFT> nearestNeighbors) {
		List<String> neighborLabels = new ArrayList<>(nearestNeighbors.size());
		for (ClassifiedFFT item : nearestNeighbors) {
			neighborLabels.add(item.getLabel());
		}

		Collections.sort(neighborLabels);

		String mostCommonItem = null;
		String currentItem = null;

		int longestStreak = 0;
		int currentStreak = 0;
		for (int i = 0; i < nearestNeighbors.size(); i++) {
			if (neighborLabels.get(i).equals(currentItem)) {
				currentStreak++;
			} else {
				if (currentStreak > longestStreak) {
					longestStreak = currentStreak;
					mostCommonItem = currentItem;
				}

				currentItem = neighborLabels.get(i);
				currentStreak = 1;
			}
		}

		if (currentStreak > longestStreak) {
			longestStreak = currentStreak;
			mostCommonItem = currentItem;
		}

		return mostCommonItem;
	}

	public boolean saveLearner(String outputFilePath) {
		try (PrintWriter writer = new PrintWriter(outputFilePath, "UTF-8")) {
			for (ClassifiedFFT fft : pointDatabase) {
				writer.println(fft.toString());
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean loadLearnerFromFile(String inputFilePath) {
		pointDatabase.clear();

		try (FileInputStream fstream = new FileInputStream(inputFilePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fstream))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				pointDatabase.add(ClassifiedFFT.fromString(strLine));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
