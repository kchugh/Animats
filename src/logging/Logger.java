package logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Logger {

	private static PrintWriter writer;
	private Logger instance;
	
	private Logger()
	{
		try {
				writer = new PrintWriter("logging/animats-actions.txt","UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	}
	public Logger getLogger()
	{
		if(instance==null)
		{
			instance = new Logger();
		}
		return instance;
	}
	public static void writeAnimatActions(long animatID, double[] input, boolean[] outputAction)
	{
		writer.println(animatID+","+Arrays.toString(input)+","+
				Arrays.toString(outputAction));
		writer.close();
	}


}
