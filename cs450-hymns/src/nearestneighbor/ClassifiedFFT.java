package nearestneighbor;

import com.eatthepath.jeospatial.util.SimpleSpatialPoint;

public class ClassifiedFFT extends SimpleSpatialPoint {

	private static final Character FIELD_DELIMITER = ';';
	private static final Character POINT_DELIMITER = ',';

	public ClassifiedFFT(String fftLabel, double[] fftData) {
		super(fftLabel, fftData);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (double value : this.getCoords()) {
			sb.append(POINT_DELIMITER + String.valueOf(value));
		}

		// Trim off leading delimiter
		String stringValue = this.getLabel() + FIELD_DELIMITER
				+ sb.substring(1);

		return stringValue;
	}

	public static ClassifiedFFT fromString(String string) {
		String[] fields = string.split(Character.toString(FIELD_DELIMITER));
		if (fields.length != 2) {
			throw new IllegalArgumentException("Malformed string");
		}

		String fftLabel = fields[0];
		String coordsString = fields[1];
		String[] pieces = coordsString.split(Character
				.toString(POINT_DELIMITER));

		double[] coords = new double[pieces.length];

		for (int i = 0; i < pieces.length; i++) {
			coords[i] = Double.valueOf(pieces[i]);
		}

		return new ClassifiedFFT(fftLabel, coords);
	}
}
