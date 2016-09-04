package com.lirfu.cardrivelearner.learner;

import java.awt.Dimension;
import java.util.TreeSet;

import javax.naming.directory.InvalidAttributeValueException;

import com.lirfu.cardrivelearner.game.Game;

public class PopulationController extends Thread {
	private int populationSize;
	private TreeSet<Unit> population;
	private int generationNumber;

	private Dimension gameWindowSize;

	private static final int TARGET_SCORE = 500;

	public PopulationController(int populationSize) throws InvalidAttributeValueException {
		if (populationSize % 2 == 0)
			throw new InvalidAttributeValueException("The population size must be odd, so that the best unit propagates directly to next generation.");

		this.populationSize = populationSize;
		this.population = new TreeSet<>();

		this.generationNumber = 0;

		this.gameWindowSize = new Dimension(200, 200);

		setDaemon(true);
	}

	@Override
	public void run() {
		super.run();
		// Create a random population.
		for (int i = 0; i < populationSize; i++) {
			Game game = new Game(gameWindowSize, false);
			DistanceSensor sensor = new DistanceSensor(game.getRoad().positionForVehicle(), 0, 10);
			sensor.mutate(true); // Randomize the starting sensor.
			Genome genome = new Genome(new Chromosome(sensor, new MovementAction(MovementAction.getRandomMovement())));

			population.add(new Unit(genome, game));
		}

		// Repeat until target score is achieved.
		do {
			generationNumber++;
			// Start units.
			for (Unit u : population)
				u.start();

			// Wait until all units are finished.
			for (Unit u : population)
				u.join();

			// Sort the population by popularity.
			reevaluate_and_sort();

			// Initialize the new generation.
			TreeSet<Unit> newGeneration = new TreeSet<>();

			// The best unit goes directly to the new generation.
			Unit bestUnit = population.last();
			newGeneration.add(bestUnit);

			// From others in pairs produce children pairs. Mutate children.
			Unit parent1 = null;
			for (Unit u : population.subSet(population.first(), bestUnit)) {
				if (parent1 != null) {
					// Mix the chromosomes of 'parent1' and 'u' to produce two children.
					Genome[] children = Genome.producePairFrom(parent1.getGenome(), u.getGenome());

					//Mutate children.
					children[0].mutate(false);
					children[1].mutate(false);

					// Add children to new generation.
					newGeneration.add(new Unit(children[0], new Game(gameWindowSize, false)));
					newGeneration.add(new Unit(children[1], new Game(gameWindowSize, false)));

					// Reset the pair counter.
					parent1 = null;
				} else
					parent1 = u;
			}

		} while (population.last().getMechanism().getScore() < TARGET_SCORE);

	}

	private void reevaluate_and_sort() {
		this.population = new TreeSet<>(this.population);
	}

	public int getGenerationNumber() {
		return this.generationNumber;
	}
}
