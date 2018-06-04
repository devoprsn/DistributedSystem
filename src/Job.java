
public class Job {
	
	private int duration;
	
	public Job()
	{
		IRandomValueGenerator rand = new RandomValueGenerator();
		//set the duration of the job
		this.duration = (rand.getTrueWithProbability(.2) ? 20000 : 10000 );	//20% chance of running for 20 seconds, otherwise, 10 seconds	
	}
	
	public Job (int duration)
	{
		this.duration = duration;
	}
	
	public int getDuration()
	{
		return duration;
	}
}
