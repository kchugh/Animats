package animats;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


public class Food extends Location{

	static final int SIZE = 20;
	JPanel panel;
	int maxX;
	int maxY;
	int amount = SIZE*SIZE;
	
	public Food(JPanel panel, int x, int y)
	{
		this.panel = panel;
		this.x = x;
		this.y = y;
	}
	public void setBounds(int maxX, int maxY)
	{
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.GREEN);
		g.fillRect(this.x, this.y, SIZE, SIZE);
		g.drawString(x+""+y, x, y);
	}
}
