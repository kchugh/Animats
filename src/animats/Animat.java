package animats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Animat {

	Color color;
	static final int SPEED = 1;
	int size;
	int x,y;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	JPanel panel;
	Animat(Color c, int size, JPanel panel, int x, int y, int maxX, int maxY)
	{
		this.color = c;
		this.size = size;
		this.panel = panel;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	public void setBounds(int maxX, int maxY)
	{
		this.maxX = maxX; this.maxY = maxY;
	}
	
	public void moveAnimat(ArrayList<Food> foodList)
	{
		
		if(foodList != null && !foodList.isEmpty())
		{
			Food closestFood = checkNearFood(foodList, x, y);
			if(closestFood!=null)
				moveTowardsFood(closestFood);
			else
				moveAnimatRandomly();
		}
		else
		{
			moveAnimatRandomly();
		}
	}
	
	private void moveAnimatRandomly()
	{
		x += speedX;
		y += speedY;
    
		if (x<0) { speedX=-speedX; x=0; }
		if (y<0) { speedY=-speedY; y=0; }
		if (x+size>maxX) { speedX=-speedX; x=maxX-size; }
		if (y+size>maxY) { speedY=-speedY; y=maxY-size; }
	}
	
	private Food checkNearFood(ArrayList<Food> foodList, int x, int y)
	{
		//check if the current position of the animat is within a circular area
		//around the food of radius 10
		for(Food food : foodList)
		{
			if(Math.sqrt(Math.pow(food.x-x,2) + Math.pow(food.y-y, 2)) <=100)
			{
				return food;
			}
		}
		return null;
	}
	
	private void moveTowardsFood(Food food)
	{
		System.out.println("Move near food");
		if(x-food.x!=0 && y-food.y!=0)
		{
			//check for relative position of animat
			if(x-food.x>0 && y-food.y>0) //top right quadrant
			{
				System.out.println("Top Right");
				x-=speedX;
				y = food.y + ((food.y-y)/(food.x-x))*(x - food.x);
				System.out.println(x+","+y);
			}
			else if(x-food.x>0 && y-food.y<0) //bottom right quadrant
			{
				System.out.println("Bottom Right");
				x-=speedX;
				y = food.y + ((food.y-y)/(food.x-x))*(x - food.x);
			}
			else if(x-food.x<0 && y-food.y<0) //bottom left quadrant
			{
				System.out.println("Bottom Left");
				x+=speedX;
				y = food.y + ((food.y-y)/(food.x-x))*(x - food.x);
			}
			else if(x-food.x<0 && y-food.y>0) //top left quadrant
			{
				System.out.println("Top Left");
				x+=speedX;
				y = food.y + ((food.y-y)/(food.x-x))*(x - food.x);
			}
		}
		else if(x-food.x==0)
		{
			if(y-food.y>0) //exactly above
				y-=speedY;
			else if(y-food.y<0)//excatly below
				y+=speedY;
		}
		else if(y-food.y==0)
		{
			if(x-food.x<0) //exactly left
				x+=speedX;
			else if(x-food.x>0)//exactly right
				x-=speedX;
		}
		
		
	}
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, size, size);
	}

}
