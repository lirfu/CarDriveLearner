package com.lirfu.cardrivelearner.learner;

import java.util.Random;
import java.util.TreeSet;

import com.lirfu.cardrivelearner.learner.Sensor.SensorData;

public class Genome implements Mutatable, Evaluable, Comparable<Genome> {
	private static long geneNumberCounter = 0;
	private static final double MUTATION_PROBABILITY = 0.1;
	private static final double SCORE_IMPORTANCE = 0.6;
	private static final double SENSOR_NUMBER_IMPORTANCE = 0.3;
	private static final double SENSOR_CHEAPNESS_IMPORTANCE = 0.1;

	private int geneNumber;
	private TreeSet<Chromosome> chromosomes;
	private int HighScore = -1;

	/** Generates a new genome with a single chromosome. */
	public Genome(Chromosome chromosome) {
		this.geneNumber = nextGeneNumber();
		this.chromosomes = new TreeSet<Chromosome>();
		addNewChromosome(chromosome);
	}

	/** Creates a new gene from the genes of two parents. */
	public Genome(Genome parent1, Genome parent2) {
		this.geneNumber = nextGeneNumber();
		Random rand = new Random();
		int cuttingPoint1 = rand.nextInt(parent1.chromosomes.size());
		int cuttingPoint2 = rand.nextInt(parent2.chromosomes.size());

		this.chromosomes = new TreeSet<Chromosome>();
		// Add some of the first parents chromosomes.
		for (int i = 0; i < cuttingPoint1; i++) {
			addNewChromosome((Chromosome) parent1.chromosomes.toArray()[i]);
		}
		// Add some of the second parents chromosomes.
		for (int i = cuttingPoint2; i < parent2.chromosomes.size(); i++) {
			addNewChromosome((Chromosome) parent2.chromosomes.toArray()[i]);
		}

		// Remove some chromosomes to forbid overflow and because the less, the
		// better.
		for (int i = 0; i < rand.nextInt(this.chromosomes.size() - 1); i++)
			chromosomes.remove(rand.nextInt(chromosomes.size()));
	}

	private int nextGeneNumber() {
		return (int) geneNumberCounter++;
	}

	/**
	 * Adds the trigger-response chromosome to the gene. If same chromosome
	 * already exists, mutate it until it's compatible.
	 */
	public void addNewChromosome(Chromosome chromosome) {
		while (!chromosomes.add(chromosome))
			chromosome.mutate(true);
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
	public void mutate(boolean force) {
		Random rand = new Random();
		int populationSize = this.chromosomes.size();
		int mutationsNumber = rand.nextInt((int) (populationSize * MUTATION_PROBABILITY));

		for (int i = 0; i < mutationsNumber; i++) {
			Chromosome chr = (Chromosome) chromosomes.toArray()[rand.nextInt(populationSize)];
			chromosomes.remove(chr);
			chr.getSensor().mutate(force);
			chr.getAction().mutate(force);
			addNewChromosome(chr);
		}
	}

	/**
	 * Returns the unique id of the gene (represents number when it was
	 * generated).
	 */
	public int getGeneNumber() {
		return this.geneNumber;
	}

	public void setHighScore(int score) {
		if (score > this.HighScore)
			this.HighScore = score;
	}

	/** Calculate goodness based an all chromosomes. */
	@Override
	public double evaluate() {
		double sensorGoodness = 0;
		for (Chromosome chr : chromosomes)
			sensorGoodness += chr.getSensor().evaluate();
		int sensorNumber = chromosomes.size();

		return SCORE_IMPORTANCE * HighScore + SENSOR_CHEAPNESS_IMPORTANCE * sensorGoodness + SENSOR_NUMBER_IMPORTANCE * sensorNumber;
	}

	/** Compares the results from the <code>evaluate()</code> function. */
	@Override
	public int compareTo(Genome o) {
		return (int) (this.evaluate() - o.evaluate());
	}

}
