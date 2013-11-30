package nearestneighbor;

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

	private String getMostCommonLabel(List<ClassifiedFFT> ffts) {
		List<String> fftLabels = new ArrayList<String>(ffts.size());
		for (ClassifiedFFT item : ffts) {
			fftLabels.add(item.getLabel());
		}

		Collections.sort(fftLabels);

		String mostCommonItem = null;
		String currentItem = null;

		int longestStreak = 0;
		int currentStreak = 0;
		for (int i = 0; i < ffts.size(); i++) {
			if (fftLabels.get(i).equals(currentItem)) {
				currentStreak++;
			} else {
				if (currentStreak > longestStreak) {
					longestStreak = currentStreak;
					mostCommonItem = currentItem;
				}

				currentItem = fftLabels.get(i);
				currentStreak = 1;
			}
		}

		if (currentStreak > longestStreak) {
			longestStreak = currentStreak;
			mostCommonItem = currentItem;
		}

		return mostCommonItem;
	}
}
