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
			
		String job=null;
		boolean empty= true; //jobs are empty or not		
		
		//continuously loop through and check if there are more jobs to give out
		while(true)
		{			      
		    empty = true;
            synchronized (jobs)
            {			  
				if(!jobs.isEmpty())
				{
                    empty=false;
                    job = jobs.removeFirst();
                }
			
             }	

             if(!empty)
             {
			    if (!idleSlaves.isEmpty())
			    {
			    	SlaveServerThread idleSlave = idleSlaves.getFirst();				    					    		
			    	idleSlave.addJob(job);
					
			    	
			    	System.out.println("Sent job to thread "+idleSlave.getID());
			    	workingSlaves.add(idleSlaves.removeFirst());
			    }			    
			    else
			    {
			    	SlaveServerThread first = workingSlaves.removeFirst();
			    	
			    		//if all slaves are working, give to 1st working slave and move it to the last position in working slaves			    		
			    		first.addJob(job);					    		
						
			    	System.out.println("Sent job to thread "+first.getID());
			    	workingSlaves.addLast(first);
			    }												    				
			
             }
                          
		}	 		
				
	}
	

	//method for the slave/slaveserverthread to tell the threadedserver when it's done. 
	// How will the slave/slaveserverthread call this method?
	public void slaveDoneMessage(int id)
	{  
		System.out.println("SlaveThreadedServer: Slave " + id+ " is done!");
		
		for (SlaveServerThread t: slaveThreads)
		{
			if (t.getId() == id)
			{
				workingSlaves.remove(t);
				idleSlaves.add(t);
			}
				
		}			
	}
	
	public LinkedList<SlaveServerThread> getIdleSlaves() 
	{
		return idleSlaves;
	}

	public LinkedList<SlaveServerThread> getWorkingSlaves() 
	{
		return workingSlaves;
	}
}

















