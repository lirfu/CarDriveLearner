package com.lirfu.cardrivelearner.learner;

import java.util.Random;

import com.lirfu.cardrivelearner.game.CarBot.Movement;

public class MovementAction implements Action {
	private static final double MUTATION_PROBABILITY = 0.1;

	private Movement move;

	public MovementAction(Movement move) {
		this.move = move;
	}

	public Movement getMove() {
		return this.move;
	}

	@Override
	public void mutate(boolean force) {
		if (force || Mutatable.willMutate(MUTATION_PROBABILITY))
			this.move = getRandomMovement();
	}

	/** TODO Auto-generated method stub */
	@Override
	public void perform(Movement move) {
	}

	public static Movement getRandomMovement() {
		return Movement.values()[new Random().nextInt(Movement.values().length)];
	}
}
