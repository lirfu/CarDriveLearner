package com.lirfu.cardrivelearner.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.lirfu.cardrivelearner.graphics.Car;
import com.lirfu.cardrivelearner.graphics.Explosion;

/**
 * The heart of the game. Calls the road generator and updates score. Takes the
 * input movement and reacts.
 */
public class CarBot extends Canvas {
	private static final long serialVersionUID = 3112659063733616160L;

	private Point position;
	private RoadGenerator road;
	private Car vehicle;
	private int Score = 0;
	private int screenPositionPercent = 50;

	private boolean threadKill;
	private int threadDelay = 500;
	private int minThreadDelay = 100;
	private boolean exploded = false;
	private boolean paused = false;

	public CarBot(double width, RoadGenerator road) {
		this.road = road;
		this.position = road.positionForVehicle();

		this.vehicle = new Car(position, (int) width);
	}

	public int getScore() {
		return this.Score;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.decode("0xffffff"));
		g.drawString("Score: " + Score + "m", 2, 12);

		Point pos = new Point(road.getWidth() * screenPositionPercent / 100, position.y);
		if (exploded)
			new Explosion(pos, 80).paint(g);
		else {
			vehicle.paint(g, pos);
		}

	}

	/** Thread that acts as a clock and generates road segments as needed. */
	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			// Initial delay.
			synchronized (thread) {
				try {
					thread.wait(3000);
				} catch (InterruptedException e) {
					System.out.println(thread.getState());
					e.printStackTrace();
				}
			}

			while (!threadKill) {
				synchronized (this) {
					Score++;
					road.nextSegment();
					new Destroyer(getIcon()).start();
					road.paintImmediately(road.getBounds());
				}
				synchronized (thread) {
					try {
						int delay = threadDelay - Score;
						do {
							thread.wait(delay < minThreadDelay ? minThreadDelay : delay);
						} while (paused);
						// Destroyer.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	});

	/** Sets threads as daemons and starts them. */
	public void start() {
		if (thread.isAlive()) {
			System.out.println("Bot is still alive.");
			return;
		}
		threadKill = false;
		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
		System.out.println("Start");
	}

	/** Stops the threads. */
	public void stop() {
		threadKill = true;
		System.out.println("Stop");
	}

	public boolean isDestroyed() {
		return this.exploded;
	}

	/** Halts current thread until car thread ends. */
	public void joinThread() {
		try {
			synchronized (thread) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Inverts the pause state of game. */
	public void togglePause() {
		paused = !paused;
		System.out.println(paused ? "Pause" : "Resume");
	}

	/** Sets the pause value. */
	public void togglePause(boolean pause) {
		paused = pause;
		System.out.println(paused ? "Pause" : "Resume");
	}

	/** Describes possible moves of the vehicle. */
	public enum Movement {
		LEFT, RIGHT
	}

	/** Perform a move on the car. Move amounts are automatically set. */
	public void perform(Movement movement) {
		if (paused || exploded)
			return;
		switch (movement) {
		case LEFT:
			screenPositionPercent -= 2;
			System.out.println("Move LEFT");
			break;
		case RIGHT:
			screenPositionPercent += 2;
			System.out.println("Move RIGHT");
			break;
		}

		// Screen boundaries.
		if (screenPositionPercent > (100 * (1 - ((double) vehicle.getWidth()) / 2 / getWidth()))) {
			screenPositionPercent = (int) (100 * (1 - ((double) vehicle.getWidth()) / 2 / getWidth()));
		} else if (screenPositionPercent < (((double) vehicle.getWidth()) / 2 / getWidth() * 100)) {
			screenPositionPercent = (int) (((double) vehicle.getWidth()) / 2 / getWidth() * 100);
		}
		road.repaint();
	}

	/** Draws an explosion and stops all threads. */
	public void demolish() {
		System.out.println("Vechicle demolished.");
		exploded = true;
		road.repaint();
		stop();
	}

	/** Returns the canvas of the vehicle. */
	public Car getIcon() {
		return new Car(vehicle);
	}

	/** Thread that checks if the car is off road and needs to be destroyed. */
	private class Destroyer extends Thread {
		private Car icon;

		protected Destroyer(Car icon) {
			this.icon = icon;
			setDaemon(true);
			setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			if (!threadKill) {
				// If car is off road
				// Destroy car.
				if (road.isVehicleOffRoad(this.icon))
					demolish();
			}
		}
	}
}
