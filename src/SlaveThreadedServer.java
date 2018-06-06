import java.util.ArrayList;
import java.util.LinkedList;

public class SlaveThreadedServer extends Thread
{	
	private LinkedList<String> jobs;
	private String[] IPAddresses;
	private int[] portNumbers;
	private LinkedList<SlaveServerThread> idleSlaves; 
	private LinkedList<SlaveServerThread> workingSlaves;
	private ArrayList<SlaveServerThread> slaveThreads;
	
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
		RedistributingObject redistributingObject = new RedistributingObject();
		
		for (int i = 0; i < THREADS; i++)
		{
			slaveThreads.add(new SlaveServerThread(IPAddresses[i], portNumbers[i], i, redistributingObject, this));
		}
		for (Thread t : slaveThreads)
		{
			t.start();
		}	
	
		System.out.println("All slavesthreads are started!");//yes!
		
		//all slaves start off as idleSlaves
		synchronized(idleSlaves)
		{
		idleSlaves.addAll(slaveThreads);
		}
			
		String job=null;
		boolean emptyJobs= true; //jobs are empty or not
		boolean emptyIdleSlaves=true; //idleSlaves empty or not
		SlaveServerThread idleSlave; //to reference an idle slave
		SlaveServerThread firstWorkingSlave; //to reference a working slave
		
		//continuously loop through and check if there are more jobs to give out
		while(true)
		{			      
		    emptyJobs = true;
		    emptyIdleSlaves = true;
		    
            synchronized (jobs)
            {			  
				if(!jobs.isEmpty())
				{
                    emptyJobs = false;
                    job = jobs.removeFirst();
                }
			
             }	

             if(!emptyJobs)
             {
            	 synchronized(idleSlaves)
            	 {
					    if (!idleSlaves.isEmpty())
					    {
					    	emptyIdleSlaves = false;
					    }
            	 }    
					 
            	 if(!emptyIdleSlaves)
            	 {
        		       synchronized(idleSlaves)
        		       {
        		    	   idleSlave = idleSlaves.removeFirst();	
        		       }				    	
				    	
				    	idleSlave.addJob(job);						
				    	
				    	System.out.println("Sent job to thread "+idleSlave.getID());
				    	//workingSlaves.add(idleSlaves.removeFirst());
				    	idleToWorking(idleSlave);         //calls synchronized method to move slave to workingSlaves
            	 }	            	 
			    else
			    {
			    	synchronized(workingSlaves)
			    	{
			    		firstWorkingSlave = workingSlaves.removeFirst();
			    	}
			    	
		    		//if all slaves are working, give to 1st working slave and move it to the last position in working slaves			    		
		    		firstWorkingSlave.addJob(job);					    		
						
			    	System.out.println("Sent job to thread "+firstWorkingSlave.getID());
			    	
			    	//move working slave to back of line of working slaves
			    	synchronized(workingSlaves)
			    	{
			    		workingSlaves.addLast(firstWorkingSlave);
			    	}
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
				//workingSlaves.remove(t);
				//idleSlaves.add(t);
				workingToIdle(t); //move slave from working to idle
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
	
	
	//this method moves a slave to working slaves
	synchronized public void idleToWorking(SlaveServerThread idleSlave)
	{	
		workingSlaves.add(idleSlave);
		System.out.println("SlaveThreadedServer: moved slave to workingSlaves!");
	}
	
	synchronized private void workingToIdle(SlaveServerThread doneSlave)
	{
		workingSlaves.remove(doneSlave);
		idleSlaves.add(doneSlave);
		
		System.out.println("SlaveThreadedServer: moved slave from working to idleSlaves!");
	}
		
    
}

















