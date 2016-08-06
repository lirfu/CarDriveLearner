package com.lirfu.cardrivelearner.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class RoadSegment extends JPanel {
	private Point start, end;

	private int width, lineWidth;

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
				g.setColor(Color.decode("0xcccc00"));
			else
				g.setColor(Color.decode("0x222222"));
			g.drawLine(start.x + i, start.y, end.x + i, end.y);
		}
		for (int i = -width / 2; i <= width / 2; i++)
			if (i > -lineWidth / 2 && i < lineWidth / 2) {
				g.setColor(Color.decode("0xcccc00"));
				g.drawLine(start.x + i, start.y + lineWidth / 2, end.x + i, end.y - lineWidth / 2);
			}
	}
}
