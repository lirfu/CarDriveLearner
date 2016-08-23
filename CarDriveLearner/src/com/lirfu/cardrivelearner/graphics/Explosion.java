package com.lirfu.cardrivelearner.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/** Graphic that represents an explosion. */
public class Explosion extends Canvas {
	private Point position;
	private int width;

	public Explosion(Point position, int width) {
		this.position = position;
		this.width = width;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Point pt = new Point(position.x - width / 2, position.y - width / 2);
		g.setColor(Color.decode("0x000000"));
		g.fillOval(pt.x, pt.y, width, (int) (4. / 5 * width));
		g.setColor(Color.decode("0xff0000"));
		g.fillOval((int) (pt.x + 1. / 5 * width), pt.y, (int) (3. / 5 * width), (int) (4. / 5 * width));
		g.setColor(Color.decode("0xff9900"));
		g.fillOval((int) (pt.x + 1. / 5 * width), (int) (pt.y + 1. / 5 * width), (int) (3. / 5 * width), (int) (2. / 5 * width));
		g.setColor(Color.decode("0xffff00"));
		g.fillOval((int) (pt.x + 2. / 5 * width), (int) (pt.y + 1. / 5 * width), (int) (1. / 5 * width), (int) (2. / 5 * width));
	}
}
