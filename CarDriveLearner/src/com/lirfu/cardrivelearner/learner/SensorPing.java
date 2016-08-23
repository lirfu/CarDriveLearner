package com.lirfu.cardrivelearner.learner;

public class SensorPing {
	private int angle;
	private int distance;

	public SensorPing(int angle, int distance) {
		this.angle = angle;
		this.distance = distance;
	}

	public int getAngle() {
		return angle;
	}

	public int getDistance() {
		return distance;
	}
}
