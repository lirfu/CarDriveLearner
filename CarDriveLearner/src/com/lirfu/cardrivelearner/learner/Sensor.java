package com.lirfu.cardrivelearner.learner;

public interface Sensor extends Mutatable {
	public boolean isTriggered(SensorData data);

	@Override
	public void mutate();

	public class SensorData {

	}
}
