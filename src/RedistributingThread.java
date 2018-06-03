import java.util.LinkedList;

//created this to deal with the load balancing that will occur at given intervals 
//will check idle slaves and working slaves and redistribute the jobs if needed

public class RedistributingThread extends Thread{
	
	private LinkedList<SlaveServerThread> idleSlaves;
	private LinkedList<SlaveServerThread> workingSlaves;
	
	public RedistributingThread(LinkedList<SlaveServerThread> idleSlaves, LinkedList<SlaveServerThread> workingSlaves)
	{
		this.idleSlaves = idleSlaves;
		this.workingSlaves = workingSlaves;
	}
	
	
    @Override
	public void run()
	{
		//needs code to communicate with with the slaveServerThread of each slave to get its list of current jobs
    	SlaveServerThread max = workingSlaves.get(0);
    	int duration, maxDuration = max.getDuration();
    	for(int i = 1; i < workingSlaves.size(); i++)
    	{
    		duration = workingSlaves.get(i).getDuration();
    		if(duration > maxDuration)
    		{
    			maxDuration = duration;
    			max = workingSlaves.get(i);
    		}
    	}
	}

}
