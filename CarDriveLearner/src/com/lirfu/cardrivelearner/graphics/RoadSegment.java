package com.lirfu.cardrivelearner.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 * Graphic of a road. It's designed to be concatenated vertically into a bending
 * road.
 */
public class RoadSegment extends Canvas {
	private Point start, end;

	private int width, lineWidth;
	private Color sideColor = Color.decode("0xcccc00");

	public RoadSegment(Point start, Point end, int width, int lineWidth) {
		this.width = width;
		this.lineWidth = lineWidth;

		this.start = start;
		this.end = end;

		if (start.y > end.y) {
			this.start = end;
			this.end = start;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = -width / 2; i <= width / 2; i++) {
			if (i < -(width / 2 - lineWidth) || i > (width / 2 - lineWidth))
				g.setColor(sideColor);
			else
				g.setColor(Color.decode("0x222222"));
			g.drawLine(start.x + i, start.y, end.x + i, end.y);
		}

		// Middle line
		// for (int i = -width / 2; i <= width / 2; i++)
		// if (i > -lineWidth / 2 && i < lineWidth / 2) {
		// g.setColor(Color.decode("0xffffff"));
		// g.drawLine(start.x + i, start.y + lineWidth / 2, end.x + i, end.y -
		// lineWidth / 2);
		// }

		// g.setColor(Color.decode("0x222222"));
		// g.drawString(toString(), 0, start.y-10);
	}

	/** Sets the color of the side lines of the road. */
	public RoadSegment setSideColor(Color color) {
		this.sideColor = color;
		return this;
	}

	/** Tells if the given coordinate is on the graphic. */
	public boolean isPointOnRoad(Point point) {
		for (int i = -width / 2; i <= width / 2; i++) {
			Line line = new Line(start.x + i, start.y, end.x + i, end.y);
			if (line.contains(new Point2D(point.x, point.y)))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RoadSegment: from (" + start.x + "," + start.y + ") to (" + end.x + "," + end.y + ")";
	}
}
