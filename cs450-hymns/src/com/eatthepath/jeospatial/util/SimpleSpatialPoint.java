package com.eatthepath.jeospatial.util;

import java.util.Arrays;
import java.util.Objects;

import com.eatthepath.jeospatial.SpatialPoint;

/**
 * <p>
 * A simple geospatial point implementation. Simple geospatial points calculate
 * distance to other points using the Haversine Formula.
 * </p>
 * 
 * @author <a href="mailto:jon.chambers@gmail.com">Jon Chambers</a>
 */
public class SimpleSpatialPoint implements SpatialPoint {
	private String label;
	private double[] coords;

	/**
	 * Constructs a new geospatial point at the given latitude and longitude
	 * coordinates.
	 * 
	 * @param latitude
	 *            the latitude of this point in degrees
	 * @param longitude
	 *            the longitude of this point in degrees
	 * 
	 * @throws IllegalArgumentException
	 *             if the given latitude is outside of the allowable range
	 */
	public SimpleSpatialPoint(String label, double[] coords) {
		this.label = label;
		this.coords = new double[coords.length];
		System.arraycopy(coords, 0, this.coords, 0, coords.length);
	}

	public SimpleSpatialPoint(SpatialPoint other) {
		this(other.getLabel(), other.getCoords());
	}

	public double[] getCoords() {
		double[] returnVal = new double[coords.length];
		System.arraycopy(coords, 0, returnVal, 0, coords.length);
		return returnVal;
	}

	public String getLabel() {
		return label;
	}

	public double getDistanceTo(SpatialPoint otherPoint) {
		return this.getDistanceTo(otherPoint.getCoords());
	}

	public double getDistanceTo(double[] coords) {
		double sum = 0;
		for (int i = 0; i < coords.length; i++) {
			sum += Math.pow(this.coords[i] - coords[i], 2);
		}

		return Math.sqrt(sum);
	}

	/**
	 * Returns a human-readable {@code String} representation of this point.
	 * 
	 * @return a {@code String} representation of this point
	 */
	@Override
	public String toString() {
		return "SimpleSpatialPoint [label=" + label + ", coords="
				+ coords.toString() + "]";
	}

	/**
	 * Generates a hash code value for this point.
	 * 
	 * @return a hash code value for this point
	 */
	@Override
	public int hashCode() {
		return Objects.hash(coords);
	}

	/**
	 * Compares this point to another object. The other object is considered
	 * equal if it is not {@code null}, is also a {@code SimpleGeospatialPoint}
	 * (or a subclass thereof), and has the same latitude and longitude as this
	 * point.
	 * 
	 * @return {@code true} if the other object is equal to this point or
	 *         {@code false} otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SpatialPoint))
			return false;
		SpatialPoint other = (SpatialPoint) obj;

		if (!this.getLabel().equals(other.getLabel()))
			return false;
		if (!Arrays.equals(coords, this.coords))
			return false;
		return true;
	}
}
