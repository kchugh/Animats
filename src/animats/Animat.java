package animats;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.swing.JPanel;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

public class Animat extends Location {

	Color color;
	static final int SPEED = 1;
	static final int FOOD_SEARCHABLE_RADIUS = 100;
	static final int PREDATOR_SEARCHABLE_RADIUS = 100;
	static final int YELL_FOOD_SEARCHABLE_RADIUS = 100;
	static final int YELL_RUN_SEARCHABLE_RADIUS = 100;
	static final int MATE_SEARCHABLE_RADIUS=100;
	static final int Allowed_Distance=5;
	static final int Allowed_Distance_Animat=30;
	static final int[] directionX={1,1,-1,0,-1,1,0,-1};
	static final int[] directionY={1,-1,-1,1,1,0,-1,0};
	Input each_input=new Input();
	boolean fowardX=true;
	boolean fowardY=true;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	double hungerValue = 0.0;
	
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
	}
	public void setBounds(int maxX, int maxY)
	{
		this.maxX = maxX; this.maxY = maxY;
	}
	
	public void moveAnimat()
	{
		//TODO: Incorporate Hunger Level
		
		getAndSetNearest(AnimatPanel.foodList,1);			
		getAndSetNearest(AnimatPanel.predatorList,2);
		getAndSetNearest(AnimatPanel.yellFoodSource,3);
		getAndSetNearest(AnimatPanel.yellPredatorSource,4);
		if(color==Color.BLUE)
			getAndSetNearest(AnimatPanel.femaleAnimat,5);
		else if(color==Color.PINK)
			getAndSetNearest(AnimatPanel.maleAnimat,5);
		each_input.display();
		
		//Load Trained Neural Network
		NeuralNetwork neuralNet = NeuralNetwork.load("trained_model/NeuralNetAnimats.nnet");
		//set input to be fed to neural network
		double[] input = {each_input.nearestFood.north,
				each_input.nearestFood.south,
				each_input.nearestFood.west,
				each_input.nearestFood.east,
				each_input.nearestPredator.north,
				each_input.nearestPredator.south,
				each_input.nearestPredator.west,
				each_input.nearestPredator.east,
				each_input.nearestMate.north,
				each_input.nearestMate.south,
				each_input.nearestMate.west,
				each_input.nearestMate.east,
				each_input.yellSignalSource.north,
				each_input.yellSignalSource.south,
				each_input.yellSignalSource.west,
				each_input.yellSignalSource.east,
				each_input.runSignalSource.north,
				each_input.runSignalSource.south,
				each_input.runSignalSource.west,
				each_input.runSignalSource.east,
				this.hungerValue};
		neuralNet.setInput(input);
		neuralNet.calculate();
		double[] networkOutput = neuralNet.getOutput();
		System.out.println(" Output: " + Arrays.toString(networkOutput));
		moveAnimatRandomly();
		
	}
	 
	void getAndSetNearest(ArrayList<?> input,int type)
	{
		double max=Integer.MAX_VALUE;
		Location ref=null;
		for(Object each:input)
		{
			Location temp=(Location)each;
			double distance= computeDistance(temp);
			if(distance<max)
			{
				ref=temp;
				max=distance;
			}
		}
		switch(type)
		{
		
		case 1:{
			if(max<FOOD_SEARCHABLE_RADIUS)
				setDirections(each_input.nearestFood,ref,FOOD_SEARCHABLE_RADIUS);
			else
				resetDirections(each_input.nearestFood);
			break;
			}
		case 2:{
			if(max<PREDATOR_SEARCHABLE_RADIUS)
				setDirections(each_input.nearestPredator,ref,PREDATOR_SEARCHABLE_RADIUS);
			else
				resetDirections(each_input.nearestPredator);
			break;
			}
		case 3:{
			if(max<YELL_FOOD_SEARCHABLE_RADIUS)
				setDirections(each_input.yellSignalSource,ref,YELL_FOOD_SEARCHABLE_RADIUS);
			else
				resetDirections(each_input.yellSignalSource);
			break;
			}
		case 4:{
			if(max<YELL_RUN_SEARCHABLE_RADIUS)
				setDirections(each_input.runSignalSource,ref,YELL_RUN_SEARCHABLE_RADIUS);
			else
				resetDirections(each_input.runSignalSource);
			break;
			}
		case 5:{
			if(max<MATE_SEARCHABLE_RADIUS)
				setDirections(each_input.nearestMate,ref,MATE_SEARCHABLE_RADIUS);
			else
				resetDirections(each_input.nearestMate);
			break;
			}
		}
	}
	void setDirections(Direction direction,Location location,double normalization_factor)
	{
		direction.setEast(computeEast(location)/normalization_factor);
		direction.setNorth(computeNorth(location)/normalization_factor);
		direction.setSouth(computeSouth(location)/normalization_factor);
		direction.setWest(computeWest(location)/normalization_factor);
	}
	
	double computeSouth(Location location)
	{
		if(location.y>y)
		{
			return location.y-y;
		}
		else
		{
			return 0;
		}
	}
	
	double computeNorth(Location location)
	{
		if(location.y<y)
		{
			return -1*(location.y-y);
		}
		else
		{
			return 0;
		}
	}
	
	double computeEast(Location location)
	{
		if(location.x>x)
		{
			return location.x-x;
		}
		else
		{
			return 0;
		}
	}
	double computeWest(Location location)
	{
		if(location.x<x)
		{
			return -1*(location.x-x);
		}
		else
		{
			return 0;
		}
	}
	
	void resetDirections(Direction direction)
	{
		direction.setEast(0);
		direction.setWest(0);
		direction.setNorth(0);
		direction.setSouth(0);
	}
	
	double computeDistance(Location temp)
	{
		return Math.sqrt((Math.pow((x-temp.x),2)+Math.pow((y-temp.y),2)));
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
		//if distance between food and animat is less than 5 units, eat and yell food
		double animatFoodDist = Math.sqrt(Math.pow(x-food.x,2) + Math.pow(y-food.y,2));
		if(animatFoodDist <= 5)
		{
			eatFood();
			yellFood(food);
		}
		
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
	
	public void eatFood(){
	
	}
		
	public void yellFood(Food food)
	{
		AnimatPanel.yellFoodSource.add(food);
	}

}
