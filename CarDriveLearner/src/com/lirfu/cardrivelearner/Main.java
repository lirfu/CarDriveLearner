package com.lirfu.cardrivelearner;

import java.awt.Color;
import java.awt.Component;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.lirfu.cardrivelearner.CarBot.Movement;

public class Main extends JFrame {
	RoadGenerator road;
	CarBot car;

	public static void main(String[] args) {
		Main myFrame = new Main(800, 600);
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(myFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
		myFrame.setResizable(false);
	}

	public Main(int width, int height) {
		setSize(width, height);

		setGlassPane(GlassPane());
		int delay = 200;
		road = new RoadGenerator(200, delay, getSize());
		car = new CarBot(60, road);

		road.add(car);
		add(road);

		// road.start();
		car.start();

		initKeyboard();
	}

	private void initKeyboard() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// Escape.
				if (e.getKeyChar() == 0x001B) {
					getGlassPane().setVisible(false);
				}
				// Control keys.
				if (e.isControlDown())
					switch (e.getKeyChar()) {
					case 0x0011:
						if (getGlassPane().isVisible()) {
							car.stop();
							// TODO Save current state.
							System.exit(0);
						}

						getGlassPane().setVisible(true);
						break;
					case 0x0010:
						car.togglePause();
						break;
					}
				// Value keys.
				switch (e.getKeyChar()) {
				case 'a':
				case 'A':
				case KeyEvent.VK_LEFT:
					car.Move(Movement.LEFT);
					break;
				case 'd':
				case 'D':
				case KeyEvent.VK_RIGHT:
					car.Move(Movement.RIGHT);
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}

	private Component GlassPane() {
		TextArea message = new TextArea("To quit press Ctrl+q again.");
		message.setSize(50, 50);
		message.setBackground(Color.decode("0x999999"));
		return message;
	}
}
