package com.lirfu.cardrivelearner.learner;

public interface Sensor extends Mutatable, Evaluable {

	/** Does the given data trigger the sensor. */
	public boolean isTriggered(SensorData data);

	/** Data that sensors can process. */
	public class SensorData {

	}
}
