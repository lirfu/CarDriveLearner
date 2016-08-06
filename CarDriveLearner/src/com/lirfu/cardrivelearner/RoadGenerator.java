package com.lirfu.cardrivelearner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

import com.lirfu.cardrivelearner.graphics.RoadSegment;

public class RoadGenerator extends JPanel {
	private int roadCenterPercent = 50;
	private int maxPoints;
	private LinkedList<Integer> roadCenterHistory;
	private Runnable calculator;

	private static Random random = new Random();
	private static int y_step = 30;
	private static int roadWidth = 200;
	private static int turnAmount = 5;

	public RoadGenerator() {
		roadCenterHistory = new LinkedList<>();

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

		setBackground(Color.decode("0x00a000"));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.decode("0x224488"));

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
		}
	}

	public void nextStep() {
		int turn = random.nextInt(turnAmount); // Next turn amount.
		turn *= random.nextInt(2) == 0 ? 1 : -1; // Next turn
													// direction.
		roadCenterPercent += turn;

		// Boundaries
		if (roadCenterPercent > (100 - getWidth() / roadWidth))
			roadCenterPercent = (100 - getWidth() / roadWidth);
		if (roadCenterPercent < (getWidth() / roadWidth))
			roadCenterPercent = (getWidth() / roadWidth);

		if (roadCenterHistory.size() > maxPoints + 1)
			roadCenterHistory.removeLast();

		roadCenterHistory.addFirst(roadCenterPercent);
	}

	public Runnable getRunnable() {
		return calculator;
	}
}
