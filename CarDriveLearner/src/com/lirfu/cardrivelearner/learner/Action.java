package com.lirfu.cardrivelearner.learner;

import com.lirfu.cardrivelearner.game.CarBot.Movement;

public interface Action extends Mutatable {
	public void perform(Movement move);
}
