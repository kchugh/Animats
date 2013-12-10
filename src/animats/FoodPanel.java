package animats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FoodPanel extends AnimatPanel implements ActionListener{

	public static ArrayList<Food> foodList = new ArrayList<Food>();
	
	public ArrayList<Food> getFoodList() {
		return foodList;
	}

	public void setFoodList(ArrayList<Food> foodList) {
		this.foodList = foodList;
	}

	public FoodPanel() {
		super();
		setPreferredSize(new Dimension(100,100));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (FoodPanel.this == e.getComponent()) {
                    for (Food food : foodList) {
                        food.setBounds(getWidth(), getHeight());
                    }
                }
            }
        });	
	}
	
	public void addFood()
	{
		Random generator = new Random();
		int foodX = generator.nextInt(400);
		int foodY = generator.nextInt(400);
		System.out.println(foodX+","+foodY);
		foodList.add(new Food(this,foodX,foodY));
		System.out.println("Before repaint");
		repaint();
	}
	
	public void removeFood()
	{
		
	}
	
	public void paintComponent(Graphics g)
	{
		System.out.println("Inside Food panel paintComponent");
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		for(Food food:foodList)
			food.draw(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Button clicked");
		
	}

}
