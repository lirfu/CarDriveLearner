package com.lirfu.cardrivelearner.learner;

import com.lirfu.cardrivelearner.CarBot.Movement;

public interface Action extends Mutatable {
	public void perform(Movement move);
}
