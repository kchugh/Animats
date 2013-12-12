package animats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AnimatPanel extends JPanel implements ActionListener{

	public static List<Animat> animats = new ArrayList<Animat>();
	public static ArrayList<Food> foodList = new ArrayList<Food>();
	public static ArrayList<Predator> predatorList = new ArrayList<Predator>();
	public static ArrayList<Animat> maleAnimat = new ArrayList<Animat>();
	public static ArrayList<Animat> femaleAnimat = new ArrayList<Animat>();
	public static ArrayList<Food> yellFoodSource = new ArrayList<Food>();
<<<<<<< HEAD
	public static ArrayList<Predator> yellPredatorSource = new ArrayList<Predator>();
=======
	public static ArrayList<Animat> yellPredatorSource = new ArrayList<Animat>();
	public static List<Child> childAnimat = new ArrayList<Child>();
>>>>>>> master
	
	private static Random randomGenerator=new Random(); 
	public AnimatPanel()
	{
		super();
		setPreferredSize(new Dimension(400,400));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (AnimatPanel.this == e.getComponent()) {
                    for (Animat animat : animats) {
                        animat.setBounds(getWidth(), getHeight());
                    }
                }
            }
        });	
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		for(Animat animat: animats)
			animat.draw(g);
		for(Food food:foodList)
			food.draw(g);
<<<<<<< HEAD
		for(Predator predator:predatorList)
			predator.draw(g);
=======
		for(Child child:childAnimat)
			child.draw(g);
		for(Food yellSource: yellFoodSource)
			yellSource.drawYell(g);
>>>>>>> master
	}
	
	public void addMalePrey()
	{
		Animat newAnimat = new Animat(Color.BLUE,35,this,randomGenerator.nextInt(getWidth()),10,getWidth(), getHeight(), Math.random());
		animats.add(newAnimat);
		maleAnimat.add(newAnimat);	
	}
	
	public void addFemalePrey()
	{
		Animat newAnimat = new Animat(Color.PINK,35,this,randomGenerator.nextInt(getWidth()),10,getWidth(), getHeight(), Math.random());
		animats.add(newAnimat);
	}
	
	public void addChildPrey()
	{
		animats.add(new Animat(Color.BLUE,20,this,10,10,getWidth(), getHeight(), Math.random()));
	}
	
	public void addPredator()
	{
		Predator newPredator = new Predator(Color.RED,35,this,10,10,getWidth(), getHeight());
		predatorList.add(newPredator);
		
	}

	public void addFood()
	{
		Random generator = new Random();
		int foodX = generator.nextInt(getWidth());
		int foodY = generator.nextInt(getHeight());
		//System.out.println(foodX+","+foodY);
		foodList.add(new Food(this,foodX,foodY));
		//System.out.println("Before repaint");
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(Animat animat : animats)
		{
			animat.moveAnimat();
		}
		for(Predator animat : predatorList)
		{
			animat.moveAnimat();
		}
		repaint();	
	}
}
