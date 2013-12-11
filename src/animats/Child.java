package animats;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Child extends Location{
	long animatID;
	Color color;
	static final int SPEED = 1;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	double hungerValue = 0.0;
	 
	static long animatObj = 0;
	boolean yellSignal=true;
	Animat mother;

	Child(Color c, int size, int x, int y, int maxX, int maxY, double hungerValue, Animat mother)
	{
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

}
