package com.lirfu.cardrivelearner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

import com.lirfu.cardrivelearner.graphics.Car;
import com.lirfu.cardrivelearner.graphics.RoadSegment;

public class RoadGenerator extends JPanel {
	private int roadCenterPercent = 50;
	private int maxPoints;
	private LinkedList<Integer> roadCenterHistory;
	private Runnable calculator;

	private static Random random = new Random();
	private static int y_step = 30;
	private static int roadWidth = 200;
	private static int turnAmount = 7;

	public RoadGenerator() {
		roadCenterHistory = new LinkedList<>();
		setBackground(Color.decode("0x00a000"));

		calculator = new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (this) {
						synchronized (roadCenterHistory) {
							nextStep();
						}
						repaint();

						try {
							calculator.wait(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		synchronized (roadCenterHistory) {
			maxPoints = getHeight() / y_step;

			if (roadCenterHistory.size() <= 1)
				return;
			int y = 0;
			int last = roadCenterHistory.getFirst();
			for (Integer percent : roadCenterHistory) {
				new RoadSegment(new Point(getWidth() * last / 100, y - y_step),
						new Point(getWidth() * percent / 100, y), roadWidth, 6).paint(g);

				y += y_step;
				last = percent;
			}

			// To test the car showing.
			new Car(new Point((getWidth() * last / 100) + roadWidth / 4, y - y_step), 0.2 * roadWidth).paint(g);
		}
	}

	private int lastTurnDirection = 0;

	public void nextStep() {
		if (getWidth() == 0)
			return;

		int turn = random.nextInt(turnAmount); // Next turn amount.
		// Next turn direction.
		turn = random.nextInt(2) == 0 ? 1 : -1;
		if (lastTurnDirection == -turn)
			turn = 0;
		lastTurnDirection = turn;
		roadCenterPercent += turn;

		// Boundaries
		if (roadCenterPercent > (100 * (1 - ((double) roadWidth) / 2 / getWidth())))
			roadCenterPercent = (int) (100 * (1 - ((double) roadWidth) / 2 / getWidth()));
		else if (roadCenterPercent < (((double) roadWidth) / 2 / getWidth() * 100))
			roadCenterPercent = (int) (((double) roadWidth) / 2 / getWidth() * 100);

		while (roadCenterHistory.size() > maxPoints)
			roadCenterHistory.removeLast();

		roadCenterHistory.addFirst(roadCenterPercent);
	}

	public Runnable getRunnable() {
		return calculator;
	}
}
