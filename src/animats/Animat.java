package animats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public class Animat extends Location {

	Color color;
	static final int SPEED = 1;
//	static final int Allowed_Distance=5;
//	static final int Allowed_Distance_Animat=30;
//	static final int[] directionX={1,1,-1,0,-1,1,0,-1};
//	static final int[] directionY={1,-1,-1,1,1,0,-1,0};
//	
//	boolean fowardX=true;
//	boolean fowardY=true;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	double hungerValue = 0.0;
	
	AnimatBrainNN adultBrainNN; 
	
	JPanel panel;
	Animat(Color c, int size, JPanel panel, int x, int y, int maxX, int maxY, double hungerValue)
	{
		this.color = c;
		this.size = size;
		this.panel = panel;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
		this.hungerValue = hungerValue;
		
		adultBrainNN = new AnimatBrainNN();
	}
	public void setBounds(int maxX, int maxY)
	{
		this.maxX = maxX; this.maxY = maxY;
	}
	
	public void moveAnimat()
	{
		
		boolean[] outputAction = adultBrainNN.run(this);
		takeAction(outputAction);
	}
	
	
	
	public void takeAction(boolean[] outputAction)
	{
		if(outputAction[0]==true)
		{
			//move north
			y -= speedY;
			if(y <=0)
				y = maxY;
			System.out.println("Move North");
		}
		else if(outputAction[1]==true)
		{
			//move South
			y += speedY;
			if(y >=maxY)
				y = 0;
			System.out.println("Move South");
		}
		else if(outputAction[2]==true)
		{
			//move West
			x -= speedX;
			if(x <=0)
				x = maxX;
			System.out.println("Move West");
		}
		else if(outputAction[3]==true)
		{
			//move east
			x += speedX;
			if(x>=maxX)
				x = 0;
			System.out.println("Move East");
		}
		
		if(outputAction[4]==true)
		{
			//eat food
			eatFood();//implement
			System.out.println("Eat Food");
		}
		if(outputAction[5]==true)
		{
			//yell food
			yellFood((Food)adultBrainNN.getNearest(AnimatPanel.foodList, this));
			System.out.println("Yell Food");
		}
		if(outputAction[6]==true)
		{
			//mate signal
			System.out.println("Mate Signal");
		}
		if(outputAction[7]==true)
		{
			//yell run
			yellRun((Animat)adultBrainNN.getNearest(AnimatPanel.predatorList, this));
			System.out.println("Yell Run");
		}
	}
	
//	private void moveAnimatRandomly()
//	{
//		Point nextPoint=getDirection(new Point(x,y), maxX, maxY, 0, 0);
//		x = nextPoint.x;
//		y = nextPoint.y;
//    
//		if (x<=0) { speedX=-speedX; x=0; fowardX=true;}
//		if (y<=0) { speedY=-speedY; y=0; fowardY=true;}
//		if (x+size>maxX) { speedX=-speedX; x=maxX-size; fowardX=false;}
//		if (y+size>maxY) { speedY=-speedY; y=maxY-size;fowardY=false; }
//	}
//	
//	Point getDirection(Point currentLocation,int maxX,int maxY,int minX,int minY)
//	{
//		ArrayList<Point> potentialCandidates=new ArrayList<Point>();
//		Random r=new Random();
//		
//			for(int i=0;i<directionX.length;i++)
//			{
//				Point check=null;
//				if(fowardX&&directionX[i]>=0&&fowardY&&directionY[i]>=0)
//				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
//				}
//				else if(!fowardX&&directionX[i]<=0&&fowardY&&directionY[i]>=0)
//				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
//				}
//				else if(fowardX&&directionX[i]>=0&&!fowardY&&directionY[i]<=0)
//				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
//				}
//				else if(!fowardX&&directionX[i]<=0&&!fowardY&&directionY[i]<=0)
//				{		check=new Point(currentLocation.x+(directionX[i]*speedX),currentLocation.y+(directionY[i]*speedY));
//				}
//				if(check!=null)
//				{	if(checkEdgeCase(check,maxX,maxY,minX,minY)&&checkIfOccupied(check))
//					{
//						potentialCandidates.add(check);
//					}
//				}
//			}
//			//System.out.println(potentialCandidates.size());
//			return potentialCandidates.get(r.nextInt(potentialCandidates.size()));
//		
//	}
//	boolean checkEdgeCase(Point p,int maxX,int maxY,int minX,int minY)
//	{
//		if(p.x<minX||p.x>maxX||p.y>maxY||p.y<minY)
//			return false;	
//		return true;
//	}
//
//	boolean checkIfOccupied(Point p)
//	{
//		if(isFood(p))
//			return false;
//		if(isAnimat(p))
//			return false;
//		return true;
//	}
//	boolean isAnimat(Point p)
//	{
//		List<Animat> checkAnimatArrayList=AnimatPanel.animats;
//		boolean result=false;
//		for(Animat eachFood:checkAnimatArrayList )
//		{
//			if(Math.abs(p.x-eachFood.x)<Allowed_Distance_Animat&&Math.abs(p.y-eachFood.y)<Allowed_Distance_Animat&&!eachFood.equals(this))
//			{
//				result=true;
//			}
//		}
//		return result;
//	}
//	boolean isFood(Point p)
//	{
//		ArrayList<Food> checkFoodArrayList=FoodPanel.foodList;
//		boolean result=false;
//		for(Food eachFood:checkFoodArrayList )
//		{
//			if(Math.abs(p.x-eachFood.x)<Allowed_Distance&&Math.abs(p.y-eachFood.y)<Allowed_Distance)
//			{
//				result=true;
//			}
//		}
//		return result;
//	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, size, size);
		g.drawString(x+" "+y, x, y);
	}
	
	public void eatFood(){
	
	}
		
	public void yellFood(Food food)
	{
		AnimatPanel.yellFoodSource.add(food);
	}
	
	public void yellRun(Animat predator)
	{
		AnimatPanel.yellPredatorSource.add(predator);
	}
	
}
