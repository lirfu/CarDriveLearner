package com.lirfu.cardrivelearner.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

/** Graphic that represents a simple car. */
public class Car extends Canvas {
	private Point position;
	private int width;

	public Car(Point startingPosition, int width) {
		this.position = startingPosition;
		this.width = width;
	}

	/** Paints the graphic at the given coordinate. */
	public void paint(Graphics g, Point position) {
		this.position = position;
		this.paint(g);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.decode("0x0"));
		g.fillOval(position.x - width / 2, position.y - width / 5, width, width / 3);
		g.fillOval(position.x - width / 2, position.y + width / 5, width, width / 3);

		g.setColor(Color.decode("0x2222cc"));
		g.fillRect((int) (position.x - 0.4 * width), position.y - width / 2, (int) (0.8 * width), width);

		// g.setColor(Color.decode("0xffffff"));
		// for (Point p : getOccupationPoints())
		// g.fillOval(p.x, p.y, 2, 2);
	}

	/**
	 * Tells if the given coordinate is on the actual graphic. <br>
	 * Currently checks only a square of the graphic.
	 */
	public boolean occupiesPoint(Point point) {
		if (point.x >= position.x - width / 2 && point.x <= position.x + width / 2 && point.y >= position.y - width / 2 && point.y <= position.y + width / 2)
			return true;
		return false;
	}

	/**
	 * Returns a list of coordinates within this graphic.<br>
	 * Currently checks only a square of the graphic.
	 */
	public LinkedList<Point> getOccupationPoints() {
		LinkedList<Point> list = new LinkedList<>();
		for (int x = position.x - width / 2; x <= position.x + width / 2; x++)
			for (int y = position.y - width / 2; y <= position.y + width / 2; y++) {
				Point pt = new Point(x, y);
				if (occupiesPoint(pt))
					list.add(pt);
			}
		return list;
	}
}
