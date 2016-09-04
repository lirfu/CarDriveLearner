package com.lirfu.cardrivelearner.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.lirfu.cardrivelearner.game.CarBot.Movement;

public class Game extends JFrame {
	private static final long serialVersionUID = -8921201870671481609L;

	private RoadGenerator road;
	private CarBot car;

	public Game(Dimension size, boolean isUserPlayer) {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setSize(size);

		setGlassPane(GlassPane());

		if (isUserPlayer)
			initKeyboard();

		int delay = 200;
		road = new RoadGenerator(size.width / 5, delay, getSize());
		car = new CarBot(size.width / 15, road);

		road.add(car);
		add(road);

		repaint();
	}

	public int getScore() {
		return this.car.getScore();
	}

	/** Checks if the game has ended. */
	public boolean isEnded() {
		return car.isDestroyed();
	}

	public void joinThread() {
		car.joinThread();
	}

	/** Initializes and starts the game thread. */
	public void startGame() {
		// road.start();
		car.start();
	}

	/** Stops the current game and starts a new one. */
	// TODO Doesn't currently work. The game starts and ends normally but the
	// window isn't repainted for some reason.
	public void restartGame() {
		removeAll();
		car.stop();
		startGame();
	}

	public CarBot getVehicle() {
		return this.car;
	}

	public RoadGenerator getRoad() {
		return this.road;
	}

	/** Adds the keyboard listeners for the game controls. */
	private void initKeyboard() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// System.out.println(e.getKeyChar());

				// Escape.
				if (e.getKeyChar() == 0x001B) {
					getGlassPane().setVisible(false);
					car.togglePause(false);
				}
				// Control keys.
				if (e.isControlDown())
					switch (e.getKeyChar()) {
					case 0x0010: // 'P'
						car.togglePause();
						break;
					case 0x0011: // 'Q'
						if (getGlassPane().isVisible()) {
							car.stop();
							// TODO Save current state.
							System.exit(0);
						}

						car.togglePause(true);
						getGlassPane().setVisible(true);
						break;
					case 0x0012: // 'R'
						restartGame();
						break;
					}
				// Value keys.
				switch (e.getKeyChar()) {
				case 'a':
				case 'A':
				case KeyEvent.VK_LEFT:
					car.perform(Movement.LEFT);
					break;
				case 'd':
				case 'D':
				case KeyEvent.VK_RIGHT:
					car.perform(Movement.RIGHT);
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

	/** Builds the exit confirmation screen. */
	private Component GlassPane() {
		TextArea message = new TextArea("To quit press Ctrl+q again.");
		message.setSize(50, 50);
		message.setBackground(Color.decode("0x999999"));
		return message;
	}
}
