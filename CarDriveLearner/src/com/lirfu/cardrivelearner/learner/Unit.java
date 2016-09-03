package com.lirfu.cardrivelearner.learner;

import com.lirfu.cardrivelearner.Game;

/** Connects the genome to the 'physical' game mechanism. */
public class Unit {
	private Game mechanism;
	private Genome genome;

	private boolean killSensoring;

	public Unit(Genome genome, Game mechanism) {
		this.mechanism = mechanism;
		this.genome = genome;

		this.killSensoring = false;
	}

	public Game getMechanism() {
		return this.mechanism;
	}

	public Genome getGenome() {
		return this.genome;
	}

	public void start() {
		mechanism.startGame();

		killSensoring = false;
		sensoringSystem.start();
	}

	public void join() {
		mechanism.joinThread();

		killSensoring = true;
		try {
			synchronized (sensoringSystem) {
				sensoringSystem.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Thread sensoringSystem = new Thread(new Runnable() {
		@Override
		public void run() {
			while (!killSensoring) {
			}
		}
	});
}
