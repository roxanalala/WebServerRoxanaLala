package gui;

import src.MyWebServer;
import src.MyWebServer2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.Object;
import java.awt.Color;
import javax.swing.JFrame;



public class GUI {


	private final JFrame frame;
	private final JPanel panel;
	private final JPanel panel2;
	private final JPanel panel3;
	public JLabel label;
	public JButton startServerButton;
	public JButton maintenanceServerButton;
	public JButton stopServerButton;
	public static int cnt=0;


	public GUI() {

		frame = new JFrame();
		frame.setSize(450,450);

		startServerButton = new JButton("Start");

//        startServerButton.addActionListener(this);
		startServerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//start action logic here
				Thread hilo = new Thread(new Runnable() {

					@Override
					public void run() {


						try {
							if (cnt>0)
							{
								MyWebServer.deactiv();
							}
							MyWebServer.activ(1);
							cnt++;

						} catch (IOException ioException) {
							ioException.printStackTrace();
						}

					}
				});
				hilo.start();


				label.setText("Server is running");
			}


		});
		maintenanceServerButton = new JButton("Maintenance");
//        maintenanceServerButton.addActionListener(this);
		maintenanceServerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Thread hilo2 = new Thread(new Runnable() {

					@Override
					public void run() {


						try {
							MyWebServer.deactiv();
							MyWebServer.activ(2);

						} catch (IOException ioException) {
							ioException.printStackTrace();
						}

					}
				});
				hilo2.start();

				//srv();
				label.setText("In maintenance");
			}
		});
		stopServerButton = new JButton("Stop");
//        stopServerButton.addActionListener(this);
		stopServerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//start action logic here

				Thread hilo3 = new Thread(new Runnable() {

					@Override
					public void run() {


						try {
							MyWebServer.deactiv();
							MyWebServer.activ(3);

						} catch (IOException ioException) {
							ioException.printStackTrace();
						}

					}
				});
				hilo3.start();

				//srv();
				label.setText("Server is stopped ");
			}
		});
		label = new JLabel("Not running");
		panel = new JPanel();
		panel.setBackground(Color.PINK);
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		panel.setLayout(new GridLayout(0, 1));
		panel.add(stopServerButton);
		//panel.add(label);


		panel2 = new JPanel();
		panel2.setBackground(Color.PINK);
		panel2.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)) ;
		panel2.add(startServerButton);
		panel2.setLayout(new GridLayout(0, 1));

		panel3 = new JPanel();
		panel3.setBackground(Color.PINK);
		panel3.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)) ;
		panel3.add(maintenanceServerButton);
		panel3.setLayout(new GridLayout(0, 1));

		frame.getContentPane().setBackground(Color.GREEN);
		frame.add(panel, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.WEST);
		frame.add(panel3, BorderLayout.EAST );
		frame.add(label, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("WebServer GUI");
		frame.pack();
		frame.setVisible(true);


	}

	public static void main(String[] args) {
		new GUI();

	}
}