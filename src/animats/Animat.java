package animats;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JPanel;

public class Animat extends Location {

	long animatID;
	Color color;
	static final int SPEED = 1;
	static final int Allowed_Distance=5;
	static final int Allowed_Distance_Animat=30;
	static final int[] directionX={1,1,-1,0,-1,1,0,-1};
	static final int[] directionY={1,-1,-1,1,1,0,-1,0};
	
	boolean fowardX=true;
	boolean fowardY=true;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	double hungerValue = 0.0;
	
	AnimatBrainNN adultBrainNN; 
	static long animatObj = 0;
	boolean yellSignal=true;
	
	
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
		this.animatID = animatObj++;
	}
	public void setBounds(int maxX, int maxY)
	{
		this.maxX = maxX; this.maxY = maxY;
	}
	
	public void moveAnimat()
	{
		
		boolean[] outputAction = adultBrainNN.run(this);
		//log animat inputm outout, actions
		
		Environment.logger.append(animatID+","+Arrays.toString(adultBrainNN.getInput(this))+","+Arrays.toString(outputAction)+"\n");
		
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
			Food food = (Food)adultBrainNN.getNearest(AnimatPanel.foodList, this);
			eatFood(food);//implement
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
			mate(adultBrainNN.mother);
			//update child mother data structure...to be implented
		}
		if(outputAction[7]==true)
		{
			//yell run
			if(AnimatPanel.predatorList!=null || !AnimatPanel.predatorList.isEmpty())
				yellRun((Animat)adultBrainNN.getNearest(AnimatPanel.predatorList, this));
			else 
			{	
				int randomDir = moveRandom();
				if(randomDir==1) //move North
					y -=speedY;
				else if(randomDir==2) //move South
					y += speedY;
				else if(randomDir==3) //move west
					x -=speedX;
				else if(randomDir==4) //move east
					x +=speedX;
			}
			System.out.println("Yell Run");
		}
	}
	private int moveRandom()
	{
		return (int)Math.random()*(4-1)+1;
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
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, size, size);
	}
	
	public void eatFood(Food food){
		if(food.amount<=0)
		{
			//remove food from the environment and from yellSource list
			if(AnimatPanel.foodList.contains(food))
			{
				AnimatPanel.foodList.remove(food);
				System.out.println("Food Removed from Environment");
			}
			if(AnimatPanel.yellFoodSource.contains(food))
			{
				AnimatPanel.yellFoodSource.remove(food);
				System.out.println("Food removed from Yell Source");
			}
		}
		else
		{
			//decrease food
			food.amount -= 0.10*food.amount;
		
			//decrease hunger value
			this.hungerValue += 0.01;
			if(this.hungerValue>=1)
			{
				int randomDir = moveRandom();
				if(randomDir==1) //move North
					y -=speedY;
				else if(randomDir==2) //move South
					y += speedY;
				else if(randomDir==3) //move west
					x -=speedX;
				else if(randomDir==4) //move east
					x +=speedX;
			}
		}
	}
		
	public void yellFood(Food food)
	{
		
		AnimatPanel.yellFoodSource.add(food);
		
	}
	
	public void yellRun(Animat predator)
	{
		System.out.println("Yell Run Called");
		if(predator!=null)
		{
			System.out.println("Mate Added"+predator.animatID);
			AnimatPanel.yellPredatorSource.add(predator);
		}
			
	}
	public void mate(Animat mother)
	{	
		System.out.println("Mother:"+mother);
		AnimatPanel.childAnimat.add(new Child(Color.BLUE,20,300,300, this.panel.getWidth(), this.panel.getHeight(), Math.random(), mother));
		System.out.println("Mating Complete. Child added");
		//make one animat say mother move in random direction after mating
		int randomDir = moveRandom();
		if(randomDir==1) //move North
			mother.y -=speedY;
		else if(randomDir==2) //move South
			mother.y += speedY;
		else if(randomDir==3) //move west
			mother.x -=speedX;
		else if(randomDir==4) //move east
			mother.x +=speedX;
		
		
	}
	

}
