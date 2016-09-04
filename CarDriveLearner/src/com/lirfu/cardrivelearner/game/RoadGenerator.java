package com.lirfu.cardrivelearner.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

import com.lirfu.cardrivelearner.graphics.Car;
import com.lirfu.cardrivelearner.graphics.RoadSegment;

public class RoadGenerator extends JPanel {
	private static final long serialVersionUID = -5431094871442664225L;

	private Dimension size;
	/** Current horizontal position of the road on the screen. */
	private int roadCenterPercent = 50;
	private int maxPoints;
	/** Holds integer percents from 0 to 100 for each segment. */
	private LinkedList<Integer> roadCenterHistory;
	/** Holds the vehicles driving on the road. */
	private LinkedList<CarBot> vehicles;

	private static Random random = new Random();
	/** Aka. road segment height. */
	private int y_step;
	private int roadWidth;
	/** Width of the edge line. */
	private int roadLineWidth;
	private static int turnAmount = 2;

	private boolean threadKill;
	private int threadDelay;

	public RoadGenerator(int roadWidth, int threadDelay, Dimension size) {
		this.size = size;
		this.roadWidth = roadWidth;
		this.y_step = (int) (0.9 * roadWidth);
		this.roadLineWidth = (int) (roadWidth * 0.03);
		this.threadDelay = threadDelay;

		roadCenterHistory = new LinkedList<>();

		// Starting straight.
		for (int i = 0; i < size.height / roadWidth + 2; i++)
			roadCenterHistory.add(50);
		vehicles = new LinkedList<>();
		setBackground(Color.decode("0x00a000"));
	}

	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() { // Starting straight.
			synchronized (this) {
				synchronized (roadCenterHistory) {
					while (maxPoints == 0) {
						repaint();
						calculateMaxPoints();
					}
					for (int i = 0; i <= maxPoints + 1; i++)
						roadCenterHistory.add(50);
					repaint();
				} // Initial delay.
				synchronized (thread) {
					try {
						thread.wait(3 * threadDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} // Draw new random segments.
				while (!threadKill) {
					synchronized (this) {
						nextSegment();
						repaint();
						synchronized (thread) {
							try {
								thread.wait(threadDelay);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	});

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		synchronized (roadCenterHistory) {
			calculateMaxPoints();

			if (roadCenterHistory.size() <= 1)
				return;
			int y = getHeight();
			int last = roadCenterHistory.getFirst();
			boolean skippedFirst = false;
			for (Integer percent : roadCenterHistory) {
				if (!skippedFirst) {
					skippedFirst = true;
					continue;
				}
				new RoadSegment(new Point(getWidth() * last / 100, y), new Point(getWidth() * percent / 100, y - y_step), roadWidth, roadLineWidth).paint(g);

				y -= y_step;
				last = percent;
			}

			for (CarBot car : vehicles) {
				car.paint(g);
			}
		}
	}

	@Override
	public Component add(Component comp) {
		vehicles.add((CarBot) comp);
		return comp;
	}

	private int lastTurnDirectionAmount = 0;

	public void nextSegment() {
		if (getWidth() == 0)
			return;

		int turn = turnAmount;
		// Next turn direction.
		int turnDirection = random.nextInt(2) == 0 ? 1 : -1;

		// Different direction than before or maximum turning achieved.
		if (Math.signum(lastTurnDirectionAmount) != turnDirection || Math.abs(lastTurnDirectionAmount) <= 3)
			lastTurnDirectionAmount += turnDirection;

		turn *= lastTurnDirectionAmount;
		roadCenterPercent += turn;

		// Screen boundaries.
		if (roadCenterPercent > (100 * (1 - ((double) roadWidth + roadLineWidth) / 2 / getWidth()))) {
			roadCenterPercent = (int) (100 * (1 - ((double) roadWidth) / 2 / getWidth()));
			lastTurnDirectionAmount = 0;
		} else if (roadCenterPercent < (((double) roadWidth + roadLineWidth) / 2 / getWidth() * 100)) {
			roadCenterPercent = (int) (((double) roadWidth) / 2 / getWidth() * 100);
			lastTurnDirectionAmount = 0;
		}

		synchronized (roadCenterHistory) {
			while (roadCenterHistory.size() > maxPoints + 1)
				roadCenterHistory.removeFirst();

			roadCenterHistory.addLast(roadCenterPercent);

			if (roadCenterHistory.size() < maxPoints + 1)
				nextSegment();
		}
	}

	public void start() {
		if (thread.isAlive()) {
			System.out.println("Road generator is still alive!");
			return;
		}
		threadKill = false;
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		threadKill = true;
	}

	public int getRoadWidth() {
		return roadWidth;
	}

	public Point positionForVehicle() {
		return new Point(roadWidth / 2, (int) (size.getHeight() - roadWidth / 2));
	}

	private void calculateMaxPoints() {
		maxPoints = getSize().height / y_step;
	}

	private RoadSegment generateRoadSegment(int center1, int center2) {
		return new RoadSegment(new Point(getWidth() * roadCenterHistory.get(center1) / 100, getHeight()), new Point(getWidth() * roadCenterHistory.get(center2) / 100, getHeight() - y_step), roadWidth,
				roadLineWidth);
	}

	/** Checks if vehicle is off road. */
	public boolean isVehicleOffRoad(Car icon) {
		RoadSegment currentSegment = generateRoadSegment(getWidth() * roadCenterHistory.get(0) / 100, getWidth() * roadCenterHistory.get(1) / 100);
		for (Point occupies : icon.getOccupationPoints())
			if (!currentSegment.isPointOnRoad(occupies))
				return true;
		return false;
	}

	/** Checks if point is off road. */
	public boolean isPointOffRoad(Point point) {
		// The lower index of the road segments center record.
		int index = roadCenterHistory.size() - (getHeight() - point.y) / y_step; // TODO Is this even correct???
		RoadSegment currentSegment = generateRoadSegment(getWidth() * roadCenterHistory.get(index) / 100, getWidth() * roadCenterHistory.get(index + 1) / 100);
		return !currentSegment.isPointOnRoad(point);
	}
}
