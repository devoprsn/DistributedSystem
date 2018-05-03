
public class Job {
	private IRandomValueGenerator rand;
	private int duration;
	
	public Job(IRandomValueGenerator rand)
	{
		this.rand = rand;
		//set the duration of the job
		this.duration = (rand.getTrueWithProbability(.2) ? 60 : 30 );		
	}
	
	public int getDuration()
	{
		return duration;
	}
}
