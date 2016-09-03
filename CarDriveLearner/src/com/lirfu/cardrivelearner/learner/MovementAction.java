package com.lirfu.cardrivelearner.learner;

import com.lirfu.cardrivelearner.CarBot.Movement;

public class MovementAction implements Action {
	private Movement move;

	public MovementAction(Movement move) {
		this.move = move;
	}

	public Movement getMove() {
		return this.move;
	}

	@Override
	public void mutate(boolean force) {
		// TODO Auto-generated method stub
	}

	@Override
	public void perform(Movement move) {
		// TODO Auto-generated method stub

	}
}
