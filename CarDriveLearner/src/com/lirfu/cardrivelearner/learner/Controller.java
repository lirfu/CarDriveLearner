package com.lirfu.cardrivelearner.learner;

import java.awt.Point;
import java.util.Random;
import java.util.TreeSet;

import com.lirfu.cardrivelearner.CarBot.Movement;
import com.lirfu.cardrivelearner.Game;

public class Controller extends Thread {
	private int populationSize;
	private TreeSet<Unit> population;

	private int generationNumber;

	private static final int TARGET_SCORE = 500;

	public Controller(int populationSize) {
		this.populationSize = populationSize;
		this.population = new TreeSet<>();

		this.generationNumber = 0;

		setDaemon(true);
	}

	@Override
	public void run() {
		super.run();
		Random rand = new Random();

		Point carPosition = new Point(0, 0);
		// Create a random population.
		for (int i = 0; i < populationSize; i++) {
			Genome genome = new Genome(
					new Chromosome(new DistanceSensor(carPosition, 0, 10), new MovementAction(Movement.values()[rand.nextInt(Movement.values().length)])));
			Game game = new Game(200, 200, false);
			population.add(new Unit(genome, game));
		}

		// Repeat until target score is achieved.
		do {
			// Start units.
			for (Unit u : population)
				u.start();

			// Wait until all units are finished.
			for (Unit u : population)
				u.join();
			// Sort the population by popularity.

			// Initialize the new generation.

			// The best unit goes directly to the new generation.

			// From others in pairs produce children pairs. Mutate children.

		} while (population.last().getMechanism().getScore() < TARGET_SCORE);

	}

	private void reevaluate_and_sort() {
		this.population = new TreeSet<>(this.population);
	}
}
