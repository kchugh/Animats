package animats;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Environment extends JFrame{
	
	public static int childId=0;
	public static PrintWriter logger;
	static final int MOVE_DELAY = 20;
	AnimatPanel animatPanelContainer = new AnimatPanel();
	FoodPanel foodPanelContainer = new FoodPanel();
	
	JButton addMalePreyButton = new JButton(new AbstractAction("Add Male Prey") {
        public void actionPerformed(ActionEvent e) {
            animatPanelContainer.addMalePrey();
        }
    });
	
	JButton addFemalePreyButton = new JButton(new AbstractAction("Add Female Prey") {
        public void actionPerformed(ActionEvent e) {
            animatPanelContainer.addFemalePrey();
        }
    });
	
	JButton addChildPreyButton = new JButton(new AbstractAction("Add Child Prey") {
        public void actionPerformed(ActionEvent e) {
            animatPanelContainer.addChildPreyWithTraining(childId++);
        }
    });
	
	JButton addPredatorButton = new JButton(new AbstractAction("Add Predator") {
        public void actionPerformed(ActionEvent e) {
            animatPanelContainer.addPredator();
        }
    });
	
	JButton addFoodButton = new JButton(new AbstractAction("Add Food") {
        public void actionPerformed(ActionEvent e) {
            animatPanelContainer.addFood();
        }
    });
	
	
	Environment()
	{
		super("Environment");
		setSize(1000,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel buttonPanel= new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		addMalePreyButton.setPreferredSize(new Dimension(50,35));
		addFemalePreyButton.setPreferredSize(new Dimension(50,35));
		addChildPreyButton.setPreferredSize(new Dimension(50,35));
		addPredatorButton.setPreferredSize(new Dimension(50,35));
		addFoodButton.setPreferredSize(new Dimension(50,35));
		
		buttonPanel.add(addMalePreyButton);
		buttonPanel.add(addFemalePreyButton);
		buttonPanel.add(addChildPreyButton);
		buttonPanel.add(addPredatorButton);
		buttonPanel.add(addFoodButton);
		
		getContentPane().add(animatPanelContainer);
		//getContentPane().add(foodPanelContainer);
		animatPanelContainer.setBorder(BorderFactory.createEmptyBorder(0,0,getWidth(), getHeight()));
		getContentPane().add(buttonPanel);
		//pack();
		new Timer(MOVE_DELAY, animatPanelContainer).start();
		
		try {
			logger = new PrintWriter("logging/animat-actions.txt","UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Environment().setVisible(true);
			}
		});
	}
}
