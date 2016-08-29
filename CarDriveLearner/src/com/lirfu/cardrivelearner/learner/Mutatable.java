package com.lirfu.cardrivelearner.learner;

import java.util.Random;

public interface Mutatable {
	/** Perform mutations to the object. */
	public void mutate();

	/**
	 * Checks if the mutation will occur.
	 * 
	 * @param probability
	 *            Calculates if the mutation will occur.
	 */
	public static boolean willMutate(double probability) {
		return new Random().nextInt((int) (100 + 100 * probability)) > 100;
	}
}
