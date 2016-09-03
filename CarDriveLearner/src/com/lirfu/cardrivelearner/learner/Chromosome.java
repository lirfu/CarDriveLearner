package com.lirfu.cardrivelearner.learner;

/** Connects the trigger-response chain. */
public class Chromosome implements Mutatable {
	private Sensor sensor;
	private Action action;

	/** Generates a random chromosome object. */
	public Chromosome() {

	}

	public Chromosome(Sensor sensor, Action action) {
		this.sensor = sensor;
		this.action = action;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public Action getAction() {
		return this.action;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Chromosome) obj).sensor.equals(sensor) && ((Chromosome) obj).action.equals(action);
	}

	/** Mutates the sensor and action. */
	@Override
	public void mutate(boolean force) {
		this.sensor.mutate(force);
		this.action.mutate(force);
	}
}
