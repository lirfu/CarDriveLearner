package com.lirfu.cardrivelearner.learner;

import java.util.LinkedList;
import java.util.Random;

import com.lirfu.cardrivelearner.learner.Sensor.SensorData;

public class Gene implements Mutatable {
	private static final double mutationProbability = 0.1;

	private LinkedList<Chromosome> chromosomes;

	public Gene() {
		this.chromosomes = new LinkedList<Chromosome>();
	}

	public Gene(Gene parent1, Gene parent2) {
		Random rand = new Random();
		int cuttingPoint1 = rand.nextInt(parent1.chromosomes.size());
		int cuttingPoint2 = rand.nextInt(parent2.chromosomes.size());

		this.chromosomes = new LinkedList<Chromosome>();
		for (int i = 0; i < cuttingPoint1; i++) {
			addNewChromosome(parent1.chromosomes.get(i));
		}
		for (int i = cuttingPoint2; i < parent2.chromosomes.size(); i++) {
			addNewChromosome(parent1.chromosomes.get(i));
		}
	}

	/** Adds the trigger-response chromosome to the gene. */
	public void addNewChromosome(Chromosome chromosome) {
		chromosomes.add(chromosome);
	}

	/**
	 * Attempts triggering gene with given data. Once the first accountered
	 * chromosome is triggered process ends and returns <code>true</code>.
	 */
	public boolean attemptTriggeringGene(SensorData data) {
		for (Chromosome chr : chromosomes)
			if (chr.getSensor().isTriggered(data))
				return true;
		return false;
	}

	/** Mutates some of the chromosomes based on the mutation probability. */
	@Override
	public void mutate() {
		Random rand = new Random();
		int populationSize = this.chromosomes.size();
		int mutationsNumber = rand.nextInt((int) (populationSize * mutationProbability));

		for (int i = 0; i < mutationsNumber; i++) {
			Chromosome chr = chromosomes.get(rand.nextInt(populationSize));
			chr.getSensor().mutate();
			chr.getAction().mutate();
		}
	}
}
