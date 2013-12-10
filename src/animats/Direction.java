package animats;

public class Direction {
	double north=0.0;
	double south=0.0;
	double west=0.0;
	double east=0.0;
	String display()
	{
		return north+","+south+","+west+","+east;
	}
	void setNorth(double val)
	{
		north=val;
	}
	void setSouth(double val)
	{
		south=val;
	}
	void setWest(double val)
	{
		west=val;
	}
	void setEast(double val)
	{
		east=val;
	}
	double getNorth()
	{
		return north;
	}
	double getSouth()
	{
		return south;
	}
	double getWest()
	{
		return west;
	}
	double getEast()
	{
		return east;
	}
	
}
