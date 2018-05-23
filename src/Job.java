
public class Job {
	private IRandomValueGenerator rand;
	private int duration;
	
	public Job(IRandomValueGenerator rand)
	{
		this.rand = rand;
		//set the duration of the job
		this.duration = (rand.getTrueWithProbability(.2) ? 60000 : 30000 );	//20% chance of running for 6 seconds, otherwise, 30 seconds	
	}
	
	public int getDuration()
	{
		return duration;
	}
}
