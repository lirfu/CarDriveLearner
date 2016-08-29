package com.lirfu.cardrivelearner.learner;

import java.util.LinkedList;

import com.lirfu.cardrivelearner.learner.Sensor.SensorData;

public class Chromosome {
	private LinkedList<Instruction> instructions;
	
	public Chromosome() {
		this.instructions = new LinkedList<Instruction>();
	}
	
	public void addInstruction(Sensor sensor, Action action) {
		instructions.add(new Instruction(sensor, action));
	}
	
	/** Attempts triggering sensors with given data. Once the first acountered sensor is triggered process ends and returns <code>true</code>. */
	public boolean attemptTriggeringSensors(SensorData data) {
		for (Instruction inst : instructions)
			if (inst.sensor.isTriggered(data))
				return true;
		return false;
	}
	
	private class Instruction {
		private Sensor sensor;
		private Action action;
		
		protected Instruction(Sensor sensor, Action action) {
			this.sensor = sensor;
			this.action = action;
		}
	}
}
