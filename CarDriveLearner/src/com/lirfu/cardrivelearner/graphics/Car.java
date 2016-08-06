package com.lirfu.cardrivelearner.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class Car extends JPanel {
	private Point position;
	private int width;

	public Car(Point position, double width) {
		this.position = position;
		this.width = (int) width;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.decode("0x0"));
		g.fillOval(position.x - width / 2, position.y - width / 5, width, width / 3);
		g.fillOval(position.x - width / 2, position.y + width / 5, width, width / 3);

		g.setColor(Color.decode("0x2222cc"));
		g.fillRect((int) (position.x - 0.4 * width), position.y - width / 2, (int) (0.8 * width), width);
	}
}
