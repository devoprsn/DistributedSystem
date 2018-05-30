import java.util.ArrayList;
import java.util.LinkedList;

public class SlaveThreadedServer extends Thread
{	
	private LinkedList<String> jobs;
	String[] IPAddresses;
	int[] portNumbers;
	private LinkedList<SlaveServerThread> idleSlaves; 
	private LinkedList<SlaveServerThread> workingSlaves;
	private ArrayList<SlaveServerThread> slaveThreads;
	
//	private RedistributingThread redistributeThread;
	
   public SlaveThreadedServer(LinkedList<String> jobs, String[] IPAddresses, int[] portNumbers)
   {
	  this.jobs = jobs;
	  this.IPAddresses = IPAddresses;
	  this.portNumbers = portNumbers;
	  idleSlaves = new LinkedList<SlaveServerThread>();
	  workingSlaves = new LinkedList<SlaveServerThread>();  
	  slaveThreads = new ArrayList<SlaveServerThread>();	  	  
   }
   

	@Override
	public void run()
	{
		System.out.println("SlaveThreadedServer initialized"); //println for testing
		
		 final int THREADS = IPAddresses.length;	
			
		for (int i = 0; i < THREADS; i++)
		{
			slaveThreads.add(new SlaveServerThread(IPAddresses[i], portNumbers[i], i, this));
		}
		for (Thread t : slaveThreads)
		{
			t.start();
		}	
	
		System.out.println("All slavesthreads are started!");//yes!
		
		//all slaves start off as idleSlaves
		idleSlaves.addAll(slaveThreads);
			
		String job;
		
		
		
		//continuously loop through and check if there are more jobs to give out
		while(true)

		   {
			
			  
			  
				if(!jobs.isEmpty())
				{
					System.out.println("Jobs are available to give out!");
					
				    if (!idleSlaves.isEmpty())
				    {
				    	SlaveServerThread idleSlave = idleSlaves.getFirst();				    	
				    	synchronized(jobs)
						{	
				    		idleSlave.addJob(jobs.removeFirst());
						}
				    	
				    	System.out.println("Sent job to thread "+idleSlave.getID());
				    	workingSlaves.add(idleSlaves.removeFirst());
				    }
				    
				    else
				    {
				    	SlaveServerThread first = workingSlaves.removeFirst();
				    	synchronized(jobs)
						{
				    		//if all slaves are working, give to 1st working slave and move it to the last position in working slaves			    		
				    		first.addJob(jobs.removeFirst());					    		
						}	
				    	System.out.println("Sent job to thread "+first.getID());
				    	workingSlaves.addLast(first);
				    }												    				
				}
				/*else
				{
					//redistribute?
				}*/
			}	 		
				
	}
	

	//method for the slave/slaveserverthread to tell the threadedserver when it's done. 
	// How will the slave/slaveserverthread call this method?
	
	public void slaveDoneMessage(int id)
	{  
		System.out.println("Slave " + id+ " is done!");
		
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

















