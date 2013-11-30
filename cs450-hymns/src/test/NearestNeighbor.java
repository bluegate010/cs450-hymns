package test;

import java.util.ArrayList;
import java.util.List;

import nearestneighbor.ClassifiedFFT;
import nearestneighbor.NNLearner;

public class NearestNeighbor {
	public static void main(String[] args) {

		List<ClassifiedFFT> ffts = new ArrayList<ClassifiedFFT>();

		ffts.add(new ClassifiedFFT("A", new double[] { 2, 3, 4 }));
		ffts.add(new ClassifiedFFT("B", new double[] { 2, 3, 3 }));
		ffts.add(new ClassifiedFFT("B", new double[] { 2, 3, 3 }));

		NNLearner learner = new NNLearner(ffts);

		System.out.println(learner.classify(new double[] { 2, 3, 4 }, 1));
	}
}
