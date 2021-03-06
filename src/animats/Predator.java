package animats;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;

public class Predator extends Location{
	long animatID;
	static long animatObj = 0;
	Color color;
	static final int SPEED = 1;
	int size;
	int maxX, maxY;
	int speedX = SPEED, speedY = SPEED;
	JPanel panel;
	int setdirection;
	Predator(Color c, int size, JPanel panel, int x, int y, int maxX, int maxY)
	{
		this.color = c;
		this.size = size;
		this.panel = panel;
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.maxY = maxY;
		this.animatID = animatObj++;
		setdirection=AnimatPanel.randomGenerator.nextInt(4);
	}
	public void moveAnimat()
	{
		moveAnimatRandomly();
	}
	private void moveAnimatRandomly()
	{
		Point nextPoint=getDirection();
		x = nextPoint.x;
		y = nextPoint.y;  
		if (x<=0) {x=maxX;}
		if (y<=0) {y=maxY;}
		if (x>=maxX) {x=0;}
		if (y>=maxY) {y=0;}
	}
	
	Point getDirection()
	{
		switch(setdirection)
		{
		case 0: return new Point(x,y-speedY);
		case 1: return new Point(x,y+speedY);
		case 2: return new Point(x+speedX,y);
		case 3: return new Point(x-speedX,y);
		default: return new Point(0,0);
		}		
	}		
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, size, size);
		//g.drawString(x+" "+y, x, y);
		g.drawOval(x+(size/2)-AnimatBrainNN.PREDATOR_SEARCHABLE_RADIUS, y+(size/2)-AnimatBrainNN.PREDATOR_SEARCHABLE_RADIUS, 2*AnimatBrainNN.PREDATOR_SEARCHABLE_RADIUS, 2*AnimatBrainNN.PREDATOR_SEARCHABLE_RADIUS);
	}

}
