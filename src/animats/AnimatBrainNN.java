package animats;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

public class AnimatBrainNN implements LearningEventListener{

	private static final int NO_OF_INPUTS = 21;
	private static final int NO_OF_OUTPUTS = 8;
	static final int FOOD_SEARCHABLE_RADIUS = 100;
	static final int PREDATOR_SEARCHABLE_RADIUS = 100;
	static final int YELL_FOOD_SEARCHABLE_RADIUS = 300;
	static final int YELL_RUN_SEARCHABLE_RADIUS = 300;
	static final int MATE_SEARCHABLE_RADIUS=25;
	
	Input each_input = new Input();
	NeuralNetwork neuralNet;
    DataSet dataSet;
    String trainingSetFileName = "data_sets/animats_data.txt";
	
	public AnimatBrainNN() {
		neuralNet =  NeuralNetwork.load("trained_model/NeuralNetAnimats.nnet");
	}
	
	public double[] getInput(Animat animat)
	{
		getAndSetNearest(AnimatPanel.foodList,1, animat);			
		getAndSetNearest(AnimatPanel.predatorList,2, animat);
		getAndSetNearest(AnimatPanel.yellFoodSource,3, animat);
		getAndSetNearest(AnimatPanel.yellPredatorSource,4, animat);
		if(animat.color==Color.BLUE)
			getAndSetNearest(AnimatPanel.femaleAnimat,5, animat);
		else if(animat.color==Color.PINK)
			getAndSetNearest(AnimatPanel.maleAnimat,5, animat);
		each_input.hungerSignal = animat.hungerValue;
		each_input.display();
		
		double[] networkInput = {each_input.nearestFood.north,
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
				animat.hungerValue};
		return networkInput;
	}
	
	public double[] setWeights(Animat animat)
	{
		double[] weights = new double[NO_OF_INPUTS];
		//set weights for Nearest Food
		setWeightsIndex(getNearest(AnimatPanel.foodList, animat),animat, weights, 0);
		
		//setWeights for Predator
		setWeightsIndex(getNearest(AnimatPanel.predatorList, animat), animat, weights, 4);
		
		//set Weights for Mate
		if(animat.color == Color.BLUE)
			setWeightsIndex(getNearest(AnimatPanel.femaleAnimat, animat), animat, weights, 8);
		else if(animat.color == Color.PINK)
			setWeightsIndex(getNearest(AnimatPanel.maleAnimat, animat), animat, weights, 8);
		
		//set Weights for Yell Food
		setWeightsIndex(getNearest(AnimatPanel.yellFoodSource, animat), animat, weights, 12);
		
		//set Weights for Yell Run
		setWeightsIndex(getNearest(AnimatPanel.yellPredatorSource, animat), animat, weights, 16);
		
		if(animat.hungerValue>0.2)
			weights[20] = animat.hungerValue;
		else
			weights[20] = 0;
		
		return weights;
	}
	
	public boolean[] run(Animat animat)
	{
		//generate input array
		double[] networkInput = getInput(animat);
		System.out.println("Input:");
		each_input.display();
		
		System.out.println("NN weights:"+Arrays.toString(neuralNet.getWeights()));
		//find weights based on distance logic
		//double[] weights = setWeights(animat);
		
		//set weights in neural net
		neuralNet.setInput(networkInput);
		
		neuralNet.calculate();
		double[] networkOutput = neuralNet.getOutput();
		System.out.println("Output:"+Arrays.toString(networkOutput));
		
		return gettingMaxIndicesFromOutput(networkOutput);
	}
	
	void setWeightsIndex(Location loc, Animat animat, double[] weights, int startIndex)
	{
		if(loc==null)
			return;
		
		double foodNorth = computeNorth(loc, animat);
		if(foodNorth!=0)
		{
			weights[startIndex] = 1/foodNorth;
		}
		else
			weights[startIndex] = 0;
		
		double foodSouth = computeSouth(loc, animat);
		if(foodSouth!=0)
		{
			weights[startIndex+1] = 1/foodSouth;
		}
		else
			weights[startIndex+1] = 0;
		
		double foodWest = computeWest(loc, animat);
		if(foodWest!=0)
		{
			weights[startIndex+2] = 1/foodWest;
		}
		else
			weights[startIndex+2] = 0;
		
		double foodEast = computeEast(loc, animat);
		if(foodEast!=0)
		{
			weights[startIndex+3] = 1/foodEast;
		}
		else
			weights[startIndex+3] = 0;
	}
	
	public Location getNearest(ArrayList<?> input, Animat animat)
	{
		double max=Integer.MAX_VALUE;
		Location ref=null;
		for(Object each:input)
		{
			Location temp=(Location)each;
			double distance= computeDistance(temp, animat);
			if(distance<max)
			{
				ref=temp;
				max=distance;
			}
		}
		return ref;
	}
	public void getAndSetNearest(ArrayList<?> input,int type, Animat animat)
	{
		if(input==null || input.isEmpty())
			return;
		double max=Integer.MAX_VALUE;
		Location ref=null;
		for(Object each:input)
		{
			Location temp=(Location)each;
			double distance= computeDistance(temp, animat);
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
			{
				if(max<=0.005)
					setVeryNear(each_input.nearestFood);
				else
					setDirections(each_input.nearestFood,ref,1, animat);
			}
				
			else
				resetDirections(each_input.nearestFood);
			break;
			}
		case 2:{
			if(max<PREDATOR_SEARCHABLE_RADIUS)
				setDirections(each_input.nearestPredator,ref,1, animat);
			else
				resetDirections(each_input.nearestPredator);
			break;
			}
		case 3:{
			if(max<YELL_FOOD_SEARCHABLE_RADIUS)
				setDirections(each_input.yellSignalSource,ref,1, animat);
			else
				resetDirections(each_input.yellSignalSource);
			break;
			}
		case 4:{
			if(max<YELL_RUN_SEARCHABLE_RADIUS)
				setDirections(each_input.runSignalSource,ref,1, animat);
			else
				resetDirections(each_input.runSignalSource);
			break;
			}
		case 5:{
			if(max<MATE_SEARCHABLE_RADIUS)
				setDirections(each_input.nearestMate,ref,1, animat);
			else
				resetDirections(each_input.nearestMate);
			break;
			}
		}
	}
	
	void setVeryNear(Direction direction)
	{
		direction.setNorth(1);
		direction.setSouth(1);
		direction.setWest(1);
		direction.setEast(1);
	}
	
	void setDirections(Direction direction,Location location,double normalization_factor, Animat animat)
	{
		direction.setEast(computeEast(location, animat)/normalization_factor);
		direction.setNorth(computeNorth(location, animat)/normalization_factor);
		direction.setSouth(computeSouth(location, animat)/normalization_factor);
		direction.setWest(computeWest(location, animat)/normalization_factor);
	}
	
	double computeSouth(Location location, Animat animat)
	{
		if(location.y>animat.y)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	double computeNorth(Location location, Animat animat)
	{
		if(location.y<animat.y)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	double computeEast(Location location, Animat animat)
	{
		if(location.x>animat.x)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	double computeWest(Location location, Animat animat)
	{
		if(location.x<animat.x)
		{
			return 1;
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
	
	double computeDistance(Location temp, Animat animat)
	{
		return Math.sqrt((Math.pow((animat.x-temp.x),2)+Math.pow((animat.y-temp.y),2)));
	}
	
	private boolean[] gettingMaxIndicesFromOutput(double[] output)
	{
		boolean[] to_be_returned=new boolean[output.length];
		double max=output[0];
		int index=0;
		for(int i=1;i<output.length;i++)
		{
			if(max<output[i])
			{
				index=i;
				max=output[i];
			}
		}
		to_be_returned[index]=true;
		for(int i=0;i<output.length;i++)
		{
			if((output[i]/max)>=0.95)
			{
				to_be_returned[i]=true;
			}
		}
		return to_be_returned;		
	}
	
	
	@Override
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
		
	}

}
