package animats;

public class Input {
	Direction nearestFood=new Direction();
	Direction nearestPredator=new Direction();
	Direction nearestMate=new Direction();
	Direction yellSignalSource=new Direction();
	Direction runSignalSource=new Direction();
	double hungerSignal=0.0;
	void display()
	{
		System.out.println("Food"+nearestFood.display());
		System.out.println("Predator"+nearestPredator.display());
		System.out.println("Mate"+nearestMate.display());
		System.out.println("YELL Food"+yellSignalSource.display());
		System.out.println("RUN"+runSignalSource.display());
		System.out.println("Hunger Value:"+hungerSignal);
		System.out.println("--------------------------*******-----------------");
	}
}
