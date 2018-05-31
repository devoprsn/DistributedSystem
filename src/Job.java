
public class Job {
	private IRandomValueGenerator rand;
	private int duration;
	
	public Job(IRandomValueGenerator rand)
	{
		this.rand = rand;
		//set the duration of the job
		this.duration = (rand.getTrueWithProbability(.2) ? 20000 : 10000 );	//20% chance of running for 20 seconds, otherwise, 10 seconds	
	}
	
	public int getDuration()
	{
		return duration;
	}
}
