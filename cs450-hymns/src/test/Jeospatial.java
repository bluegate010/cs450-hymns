package test;

import java.util.List;

import com.eatthepath.jeospatial.util.SimpleSpatialPoint;
import com.eatthepath.jeospatial.vptree.VPTree;

public class Jeospatial {

	public static void main(String[] args) {
		VPTree<SimpleSpatialPoint> pointDatabase = new VPTree<SimpleSpatialPoint>();
		for (int i = -10; i < 11; i++) {
			for (int j = -10; j < 11; j++) {
				SimpleSpatialPoint p = new SimpleSpatialPoint(String.format(
						"point:%d:%d", i, j), i, j);
				pointDatabase.add(p);
			}
		}

		SimpleSpatialPoint searchPoint = new SimpleSpatialPoint("point-1", 3.5,
				3.5);

		// Find the ten nearest zip codes to Davis Square
		List<SimpleSpatialPoint> nearest = pointDatabase.getNearestNeighbors(
				searchPoint, 4);

		for (SimpleSpatialPoint p : nearest) {
			System.out.println(p);
		}
	}
}
