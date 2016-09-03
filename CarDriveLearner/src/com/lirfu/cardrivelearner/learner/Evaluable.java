package com.lirfu.cardrivelearner.learner;

/** So object can be evaluated for it's goodness. */
public interface Evaluable {

	/**
	 * Evaluate the goodness of the object.
	 * 
	 * @return The goodness of the object. It's evaluated from 0 to MAXGOODNESS.
	 */
	public double evaluate();

}
