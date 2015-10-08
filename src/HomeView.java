import java.awt.*;
import java.awt.event.*	;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;


public class HomeView extends JFrame{
	private Model model;
	/*
	 * Constructor that constructs all components for the main window
	 */
	public HomeView() 
	{
		
		
		try {
			 
		 final BufferedImage backgroundImage = ImageIO.read(new File("img/background.jpg"));
		  final JPanel container = new JPanel(){
				@Override
				protected void paintComponent(Graphics g) {
					
					super.paintComponent(g);
					g.drawImage(backgroundImage, 0, 0, null);
				}
			};
			JButton circleTheme = new JButton("Circle Theme");
	        JButton squareTheme = new JButton("Square Theme");
			
	        final JTextField numStones = new JTextField("4");
	        circleTheme.setPreferredSize(new Dimension(200,200));
			squareTheme.setPreferredSize(new Dimension(200,200));
			numStones.setPreferredSize(new Dimension(200, 200));
			numStones.setFont(new Font("Arial Black", Font.BOLD, 50));
			numStones.setHorizontalAlignment(JTextField.CENTER);
	        container.add(circleTheme);
	        container.add(squareTheme);
	        container.add(numStones);
	        
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setContentPane(container);
	        setVisible(true);
	        pack();
	        
	        circleTheme.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                int stones = Integer.parseInt(numStones.getText());
	                if (stones > 4 || stones < 3) {
	                    JOptionPane.showMessageDialog(container, "Enter 3 OR 4 stones to begin with");
	                } 
	                else {
	                	model = new Model(stones);
	                	setVisible(false);
	                    GameFrame gf = new GameFrame(model, new CircleTheme());
	                    gf.setVisible(true);
	                }
	            }
	        });

	        squareTheme.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                int stones = Integer.parseInt(numStones.getText());
	                if (stones > 4 || stones < 3) {
	                    JOptionPane.showMessageDialog(container, "Enter 3 OR 4 stones to begin with");
	                } else {
	                   
	                    model = new Model(stones);
	                    setVisible(false);
	                    GameFrame gf = new GameFrame(model, new SquareTheme());
	                    gf.setVisible(true);
	                }
	            }
	        });
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
