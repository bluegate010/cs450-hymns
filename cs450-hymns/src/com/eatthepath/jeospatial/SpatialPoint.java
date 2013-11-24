package com.eatthepath.jeospatial;

public interface SpatialPoint {
	public double getDistanceTo(SpatialPoint otherPoint);

	public double[] getCoords();

	public String getLabel();
}
