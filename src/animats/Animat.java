package animats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class Animat {

	Color color;
	static final int SPEED = 1;
	static final int Allowed_Distance=5;
	static final int Allowed_Distance_Animat=30;
	static final int[] directionX={1,1,-1,0,-1,1,0,-1};
	static final int[] directionY={1,-1,-1,1,1,0,-1,0};
	boolean fowardX=true;
	boolean fowardY=true;
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
	
	public void moveAnimat()
	{
		//TODO: Incorporate Hunger Level
		if(AnimatPanel.foodList != null && !AnimatPanel.foodList.isEmpty())
	     {
			Location foodlocation=getNearest(AnimatPanel.foodList);
			System.out.println(foodlocation.x+" "+foodlocation.y);
//	             if(foodlocation!=null)
//	                     moveTowardsFood((Food)foodlocation);
//	             else
//	                     moveAnimatRandomly();
	     }
	     else
	     {
	             moveAnimatRandomly();
	     }
		
		
		
	}
	 
	Location getNearest(ArrayList<Food> input)
	{
		double max=Integer.MAX_VALUE;
		Location ref=null;
		for(Food each:input)
		{
			Location temp=each;
			double distance=Math.sqrt((Math.pow((x-temp.x),2)+Math.pow((y-temp.y),2)));
			System.out.println("----------In FOod"+temp.x+" "+temp.y+"Distance"+distance);
			if(distance<max)
			{
				ref=temp;
				max=distance;
			}
		}
		return ref;
	}
	
	private void moveAnimatRandomly()
	{
		Point nextPoint=getDirection(new Point(x,y), maxX, maxY, 0, 0);
		x = nextPoint.x;
		y = nextPoint.y;
    
		if (x<=0) { speedX=-speedX; x=0; fowardX=true;}
		if (y<=0) { speedY=-speedY; y=0; fowardY=true;}
		if (x+size>maxX) { speedX=-speedX; x=maxX-size; fowardX=false;}
		if (y+size>maxY) { speedY=-speedY; y=maxY-size;fowardY=false; }
	}
	
	Point getDirection(Point currentLocation,int maxX,int maxY,int minX,int minY)
	{
		ArrayList<Point> potentialCandidates=new ArrayList<Point>();
		Random r=new Random();
		
			for(int i=0;i<directionX.length;i++)
			{
				Point check=null;
				if(fowardX&&directionX[i]>=0&&fowardY&&directionY[i]>=0)
				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
				}
				else if(!fowardX&&directionX[i]<=0&&fowardY&&directionY[i]>=0)
				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
				}
				else if(fowardX&&directionX[i]>=0&&!fowardY&&directionY[i]<=0)
				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
				}
				else if(!fowardX&&directionX[i]<=0&&!fowardY&&directionY[i]<=0)
				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
				}
				if(check!=null)
				{	if(checkEdgeCase(check,maxX,maxY,minX,minY)&&checkIfOccupied(check))
					{
						potentialCandidates.add(check);
					}
				}
			}
			//System.out.println(potentialCandidates.size());
			return potentialCandidates.get(r.nextInt(potentialCandidates.size()));
		
	}
	boolean checkEdgeCase(Point p,int maxX,int maxY,int minX,int minY)
	{
		if(p.x<minX||p.x>maxX||p.y>maxY||p.y<minY)
			return false;	
		return true;
	}

	boolean checkIfOccupied(Point p)
	{
		if(isFood(p))
			return false;
		if(isAnimat(p))
			return false;
		return true;
	}
	boolean isAnimat(Point p)
	{
		List<Animat> checkAnimatArrayList=AnimatPanel.animats;
		boolean result=false;
		for(Animat eachFood:checkAnimatArrayList )
		{
			if(Math.abs(p.x-eachFood.x)<Allowed_Distance_Animat&&Math.abs(p.y-eachFood.y)<Allowed_Distance_Animat&&!eachFood.equals(this))
			{
				result=true;
			}
		}
		return result;
	}
	boolean isFood(Point p)
	{
		ArrayList<Food> checkFoodArrayList=FoodPanel.foodList;
		boolean result=false;
		for(Food eachFood:checkFoodArrayList )
		{
			if(Math.abs(p.x-eachFood.x)<Allowed_Distance&&Math.abs(p.y-eachFood.y)<Allowed_Distance)
			{
				result=true;
			}
		}
		return result;
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
