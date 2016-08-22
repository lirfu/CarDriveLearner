package com.lirfu.cardrivelearner;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.lirfu.cardrivelearner.graphics.Car;
import com.lirfu.cardrivelearner.graphics.Explosion;

public class CarBot extends Canvas {
	private Point position;
	private RoadGenerator road;
	private Car vechicle;
	private int metersPassed = 0;
	private int screenPositionPercent = 50;

	private boolean threadKill;
	private int threadDelay = 500;
	private int minThreadDelay = 100;
	private boolean exploded = false;
	private boolean paused = false;

	public CarBot(double width, RoadGenerator road) {
		this.road = road;
		this.position = road.positionForVehicle();

		this.vechicle = new Car(position, (int) width);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.decode("0xffffff"));
		g.drawString("Score: " + metersPassed + "m", 2, 12);

		Point pos = new Point(road.getWidth() * screenPositionPercent / 100, position.y);
		if (exploded)
			new Explosion(pos, 80).paint(g);
		else {
			vechicle.paint(g, pos);
		}

	}

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
					metersPassed++;
					road.nextSegment();
				}
				road.repaint();

				synchronized (thread) {
					try {
						int delay = threadDelay - metersPassed;
						do {
							thread.wait(delay < minThreadDelay ? minThreadDelay : delay);
						} while (paused);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	});

	private Thread Destroyer = new Thread(new Runnable() {
		@Override
		public void run() {
			// Initial delay.
			synchronized (Destroyer) {
				try {
					Destroyer.wait(3000);
				} catch (InterruptedException e) {
					System.out.println(Destroyer.getState());
					e.printStackTrace();
				}
			}
			while (!threadKill) {
				// If car is off road
				// Destroy car.
				if (road.vehicleOffRoad(CarBot.this))
					demolish();
				synchronized (Destroyer) {
					try {
						Destroyer.wait(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	});

	public void start() {
		if (thread.isAlive()) {
			System.out.println("Bot is still alive.");
			return;
		}
		threadKill = false;
		thread.setDaemon(true);
		thread.start();
		Destroyer.setDaemon(true);
		Destroyer.start();
		System.out.println("Start");
	}

	public void stop() {
		threadKill = true;
		System.out.println("Stop");
	}

	public void togglePause() {
		paused = !paused;
		System.out.println(paused ? "Pause" : "Resume");
	}

	public void togglePause(boolean pause) {
		paused = pause;
		System.out.println(paused ? "Pause" : "Resume");
	}

	public enum Movement {
		LEFT, RIGHT
	}

	public void Move(Movement movement) {
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
		if (screenPositionPercent > (100 * (1 - ((double) vechicle.getWidth()) / 2 / getWidth()))) {
			screenPositionPercent = (int) (100 * (1 - ((double) vechicle.getWidth()) / 2 / getWidth()));
		} else if (screenPositionPercent < (((double) vechicle.getWidth()) / 2 / getWidth() * 100)) {
			screenPositionPercent = (int) (((double) vechicle.getWidth()) / 2 / getWidth() * 100);
		}

		road.repaint();
	}

	public void demolish() {
		exploded = true;
		road.repaint();
		stop();
	}

	public Car getIcon() {
		return vechicle;
	}
}
