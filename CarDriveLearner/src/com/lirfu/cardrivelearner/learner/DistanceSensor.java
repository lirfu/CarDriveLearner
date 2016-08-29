package com.lirfu.cardrivelearner.learner;

public class DistanceSensor implements Sensor {
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
}
