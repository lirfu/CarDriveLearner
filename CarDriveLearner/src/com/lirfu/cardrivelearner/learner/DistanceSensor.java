package com.lirfu.cardrivelearner.learner;

public class DistanceSensor implements Sensor {
	private static final double mutationProbability = 0.4;
	private static final double angleMutationProbability = 0.4;
	private static final double distanceMutationProbability = 0.4;

	private int angle;
	private int triggerDistance;

	public DistanceSensor(int angle, int triggerDistance) {
		this.angle = angle;
		this.triggerDistance = triggerDistance;
	}

	public int getAngle() {
		return angle;
	}

	public int getTriggerDistance() {
		return triggerDistance;
	}

	@Override
	public boolean isTriggered(SensorData data) {
		return false;
	}

	@Override
	public void mutate() {
		if (!Mutatable.willMutate(mutationProbability))
			return;

	}

	@Override
	public boolean equals(Object obj) {
		// Only 1 sensor can cover a certain angle.
		return this.angle == ((DistanceSensor) obj).angle;
	}
}
