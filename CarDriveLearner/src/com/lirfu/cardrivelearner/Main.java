package com.lirfu.cardrivelearner;

import javax.swing.JFrame;

public class Main extends JFrame {

	public static void main(String[] args) {
		Main myFrame = new Main();
		myFrame.setSize(400, 500);
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(myFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}

	public Main() {
		RoadGenerator road = new RoadGenerator();
		add(road);

		Thread road_thread = new Thread(road.getRunnable());
		road_thread.setDaemon(true);
		road_thread.start();
	}
}
