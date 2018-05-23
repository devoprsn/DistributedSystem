import java.util.ArrayList;
import java.util.LinkedList;

public class SlaveThreadedServer implements Runnable
{
	private String[] iPAddresses;
	private int [] portNumbers;
	
	private LinkedList<String> jobs;
	private LinkedList<SlaveServerThread> idleSlaves; 
	private LinkedList<SlaveServerThread> workingSlaves;
	private ArrayList<SlaveServerThread> slaveThreads;
	
	private RedistributingThread redistributeThread;
	
   public SlaveThreadedServer(String[] iPAddresses, int [] portNumbers, LinkedList<String> jobs)
   {
	  this.iPAddresses = iPAddresses;
	  this.portNumbers = portNumbers;
	  this.jobs = jobs;
	  
	  idleSlaves = new LinkedList<SlaveServerThread>();
	  workingSlaves = new LinkedList<SlaveServerThread>();
	  
	  slaveThreads = new ArrayList<SlaveServerThread>();
	  
	  //instantiate a thread that will take care of load balancing. redistributing the jobs every so often
	  redistributeThread = new RedistributingThread(idleSlaves,workingSlaves);  
	  
	  
   }
   

	@Override
	public void run()
	{
		final int THREADS = 4;	
		
		for (int i = 0; i < THREADS; i++)
		{
			slaveThreads.add(new SlaveServerThread(iPAddresses[i], portNumbers[i], i , this));
		}
		for (Thread t : slaveThreads)
		{
			t.start();
		}
		
		
		//start all slaves off on the idleSlaves list:		
		idleSlaves.addAll(slaveThreads);
		
		//start the redistributing thread:
		redistributeThread.start();
		
		
		
		//continuously loop through and check if there are more jobs to give out
		while(true)
		{
			if(!jobs.isEmpty())
			{
				
			    if (!idleSlaves.isEmpty())
			    {
			    	synchronized(jobs)
					{
			    		idleSlaves.getFirst().addJob(jobs.removeFirst());
					}
			    	workingSlaves.add(idleSlaves.removeFirst());
			    }
			    
			    else
			    {
			    	synchronized(jobs)
					{
			    		//if all slaves are working, give to 1st working slave and move it to the last position in working slaves
			    		SlaveServerThread first = workingSlaves.removeFirst();
			    		first.addJob(jobs.removeFirst());
			    		workingSlaves.addLast(first);
					}
			    	
			    }			
								    				
			}
		}
			
	}
	
	
	//method for the slave/slaveserverthread to tell the threadedserver when it's done. 
	// How will the slave/slaveserverthread call this method?
	
	public void slaveDoneMessage(int id)
	{  
		
		for (SlaveServerThread t: slaveThreads)
		{
			if (t.getId() == id)
			{
				workingSlaves.remove(t);
				idleSlaves.add(t);
			}
				
		}
	}
}

















