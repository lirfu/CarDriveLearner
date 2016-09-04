package com.lirfu.cardrivelearner.learner;

import java.awt.Point;
import java.util.Random;

import com.lirfu.cardrivelearner.game.RoadGenerator;

public class DistanceSensor implements Sensor {
	private static final double MUTATION_PROBABILITY = 0.4;
	private static final double ANGLE_MUTATION_PROBABILITY = 0.4;
	private static final double DISTANCE_MUTATION_PROBABILITY = 0.4;
	private static final int MAX_DISTANCE = 60;

	private Point position;
	/**
	 * Euclidian angle, 0° is on positive x.<br>
	 * Spans from 0° to -180°
	 */
	private int angle;
	private int triggerDistance;

	public DistanceSensor(Point position, int angle, int triggerDistance) {
		this.position = position;
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
		for (int x = position.x; x < position.x + triggerDistance * Math.cos(angle); x++)
			// If a point on sensors line of sight is off road, trigger sensor.
			if (((RoadGenerator) data.getData()).isPointOffRoad(new Point(x, (int) (position.y + x * Math.tan(angle)))))
				return true;

		return false;
	}

	@Override
	public void mutate(boolean force) {
		if (force || !Mutatable.willMutate(MUTATION_PROBABILITY))
			return;

		Random rand = new Random();
		if (force || Mutatable.willMutate(ANGLE_MUTATION_PROBABILITY)) {
			this.angle = rand.nextInt(180);
		}
		if (force || Mutatable.willMutate(DISTANCE_MUTATION_PROBABILITY)) {
			this.triggerDistance = rand.nextInt(MAX_DISTANCE);
		}
	}

	@Override
	public boolean equals(Object obj) {
		// Only 1 sensor can cover a certain angle.
		return this.angle == ((DistanceSensor) obj).angle;
	}

	@Override
	public double evaluate() {
		return this.triggerDistance / MAX_DISTANCE;
	}
}
