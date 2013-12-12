package animats;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Child extends Location{
	long animatID;
	Color color;
	static final int SPEED = 1;
	File childFile;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	int trainingStep=0;
	double hungerValue = 0.0;
	String filename; 
	static long animatObj = 0;
	boolean yellSignal=true;
	static final int MAX_TRAINING_STEPS=10000;
	Animat mother;

	Child(Color c, int size, int x, int y, int maxX, int maxY, double hungerValue, Animat mother)
	{
		animatID=animatObj++;
		filename="ChildTrainingFile"+animatID+".txt";
		this.childFile=new File(filename);
		this.color = c;
		this.size = size;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
		this.hungerValue = hungerValue;
		this.mother = mother;
	}
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(mother.x+5, mother.y+5, size, size);
	}
	public void write(double[] input,double[] output)
	{
		trainingStep++;
		if(trainingStep<MAX_TRAINING_STEPS)
		{
		try
		{
			if (!childFile.exists()) {
				childFile.createNewFile();
			}
			FileWriter fw = new FileWriter(childFile.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(tabSeperatedString(input)+"\t"+tabSeperatedString(output)+"\n");
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}
	}
	String tabSeperatedString(double s[])
	{
		String toBeRetured="";
		for(int i=0;i<s.length-1;i++)
		{
			toBeRetured+=s[i]+"\t";
		}
		toBeRetured+=s[s.length-1];
		return toBeRetured;
	}
}