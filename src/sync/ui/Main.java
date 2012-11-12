package sync.ui;

import sync.receive.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main {

	private static JTextField username = new JTextField("mailtest10000@163.com");
	private static JTextField password = new JTextField("qwerty");
	
	private static JButton confirmButton = new JButton("Confirm");
	private static JButton cancelButton = new JButton("Cancel");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Netease 163 E-mail Server");
		frame.setSize(500, 200);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,2));
		panel.setBorder(BorderFactory.createTitledBorder("Log in"));
		
		panel.add(new JLabel("Username: "));
		panel.add(username);
		panel.add(new JLabel("Password: "));
		panel.add(password);
		
		panel.add(confirmButton);
		panel.add(cancelButton);
		
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Arguments.username = username.getText();
				Arguments.password = password.getText();
				frame.setVisible(false);
				Core.createInstance();
			}
			
		});

		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});

		frame.add(panel);
		frame.setVisible(true);
	}
}
