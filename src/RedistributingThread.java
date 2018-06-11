import java.util.LinkedList;

//created this to deal with the load balancing that will occur at given intervals 
//will check idle slaves and working slaves and redistribute the jobs if needed

public class RedistributingThread extends Thread{
	
	private LinkedList<SlaveServerThread> idleSlaves;
	private LinkedList<SlaveServerThread> workingSlaves;
//	private RedistributingObject redistributingObject;
	
	public RedistributingThread(LinkedList<SlaveServerThread> idleSlaves, LinkedList<SlaveServerThread> workingSlaves)
	{
		this.idleSlaves = idleSlaves;
		this.workingSlaves = workingSlaves;
//		this.redistributingObject = redistributingObject;
	}
	
	
    @Override
	public void run()
	{
		//sleeps for some time and then goes through the algorithm and redistributes the jobs if needed.
    	//keeps repeating this...
    	
    	/* Algorithm: if there is an idle slave: it finds the working slave with the most jobs and 
    	 * if the maxJobsSlave has at least a certain # of jobs it gives 1/2 of those jobs to the 1st (or only) idle slave.
    	 */
    	
    	System.out.println("Redistributing Thread initialized");
         
    	SlaveServerThread maxWorkSlave;   //slave with the most time left to complete its tasks
    	int totalDuration, maxTotalDuration;  //time left for each slave to complete all their tasks
    	int numTasksLeft;              //number of tasks the slave with the longest total duration has left
    	int jobsToRedistribute;        //number of tasks to redistribute
    	SlaveServerThread idleSlave = null;   //slave that jobs will be redistributed to
    	boolean emptyIdleSlaves = true;          //if idleSlaves is empty or not
    	boolean emptyWorkingSlaves=true;
    	int size = 0;                  //# of working slaves
    	
    	while(true)
    	{
    		emptyIdleSlaves = true;
    		emptyWorkingSlaves = true;
		
	    	try {
				sleep(5000);
			}
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
		    	
		    	
	    	synchronized (idleSlaves)
	    	{
	    		if(!idleSlaves.isEmpty())
	    		{
	    			emptyIdleSlaves = false;
	    		}	    		
	    	}	    
	    	
	    	if(!emptyIdleSlaves)  //only redistribute if there is one or more idle slaves and working slaves
	    	{
	    		System.out.println("RedistributingThread: idle slave (s) found");
	    		synchronized(workingSlaves)
		    	{
		    		if(!workingSlaves.isEmpty())
		    		{
		    			emptyWorkingSlaves = false;
		    		}
		    	}
	    		
	    		if(!emptyWorkingSlaves)
	    		{
	    			System.out.println("RedistributingThread: working slave (s) found");
	    			
	    		    synchronized(workingSlaves)
	    		    {
	    		    	maxWorkSlave = workingSlaves.get(0);  
	    		    }
	    		    
	    		    
	    		    //request slaveSlaveServerThread to ask slave for totalDuration of all tasks, then slaveServerThread sets the value in the redistributingObject
	    		    maxWorkSlave.getTotalDurationOfAllTasks();  
	    		    
	    		    //server thread should wait until value is updated
	    		    while(maxWorkSlave.getRedistributingObject().getTotalDuration() == -1) {}
	    		    
	    		    maxTotalDuration = maxWorkSlave.getRedistributingObject().getTotalDuration();
	    		    
	    		    System.out.println("RedistributingThread: Max total duration for thread " + maxWorkSlave.getID()
	    			+ " = " + maxTotalDuration + " seconds");
	    		    
	    		    //now reset totalDuration to -1, prior to next read, so no stale value is read
	    		    maxWorkSlave.getRedistributingObject().setTotalDuration(-1);
			    	
	    		   // maxTotalDuration = 0; ///why?

    		    	synchronized(workingSlaves)  //synchronize on whole loop so size of workingSlaves shouldn't change mid-loop
			    	{
					    size = workingSlaves.size();	    					    					    	
					    	
				    	SlaveServerThread slave = null;
				    	for(int i = 1; i < size; i++)
				    	{						    		
				    		slave = workingSlaves.get(i);
				    		
				    		slave.getTotalDurationOfAllTasks();
				    		
				    		//slave should wait until value is updated
				    		while(slave.getRedistributingObject().getTotalDuration() == -1) {}
				    		
				    		//
				    		totalDuration = slave.getRedistributingObject().getTotalDuration();
				    		System.out.println("RedistributingThread: Total duration left for thread " + slave.getID()
			    			+ " = " + (totalDuration / 1000)  + " seconds");
				    		slave.getRedistributingObject().setTotalDuration(-1);
				    		if(totalDuration > maxTotalDuration)
				    		{
				    			maxTotalDuration = totalDuration;
				    			maxWorkSlave = slave;
				    		}
	
				    	}	

				    	
	    		   }  	
			    	
    		    	System.out.println("MaxWorkSlave is Slave " + maxWorkSlave.getID());
    		    	
    		    	//got our slave with longest time until finished all tasks, now count how many tasks he has left
    		    	
    		    	////request slaveSlaveServerThread to ask slave for count of all tasks, then slaveServerThread sets the value in the redistributingObject
			    	maxWorkSlave.getCountOfTasks();
			    	
			    	//slave should wait until value is updated
			    	while(maxWorkSlave.getRedistributingObject().getNumTasksLeft() == -1) {}
			    	
			    	numTasksLeft = maxWorkSlave.getRedistributingObject().getNumTasksLeft();
			    	System.out.println("RedistributingThread: Num tasks left for thread " + maxWorkSlave.getID()
			    			+ " = " + numTasksLeft);
			    	maxWorkSlave.getRedistributingObject().setNumTasksLeft(-1);
			    		
			    	
			    	if (numTasksLeft>=2) 
			    	{
			    		//so redistribute:	
			    		
			    		
			    		System.out.println("RedistributingThread: need to redistribute");
			    		
			    		//get the first (or only) idle slave and remove it from the list of idleSlaves:
			    		
			    		synchronized(idleSlaves)
			    		{
			    			idleSlave = idleSlaves.removeFirst();
			    		}
			    		
			    		
			    		  //now decide how to give away the jobs
			    		if (numTasksLeft%2 == 0)  //even amount
			    		{
			    			jobsToRedistribute = numTasksLeft/2;
			    		}
			    		else  //odd amount
			    		{
			    			jobsToRedistribute = (numTasksLeft-1)/2;
			    		}		    		
			    	    
			    	    int i = 0;
		    	    
			    	    while(i<jobsToRedistribute)
			    	    {
			    	    	////removes job and commands slave to send duration of removed task. slaveServerThread sets the value in the redistributingObject
			    	    	maxWorkSlave.removeJob(); 				    	    	
			    	    	while(maxWorkSlave.getRedistributingObject().getDurationOfRemovedTask() == -1) {}
			    	    	
			    	    	int duration = maxWorkSlave.getRedistributingObject().getDurationOfRemovedTask();
			    	    	
			    	    	System.out.println("RedistributingThread: Duration of removed task for thread " 
			    	    	+ maxWorkSlave.getID() + " = " + duration + " seconds");
			    	    	maxWorkSlave.getRedistributingObject().setDurationOfRemovedTask(-1);
			    	    	
			    	    	idleSlave.addJobWithDuration(duration);  // add the job with the duration to the idle slave
			    	    	i++;
			    	    }		    	    	
			    	    
			    	    //now add the idleSlave to the list of workingSlaves
			    	    synchronized(workingSlaves)
				    	{
				    		workingSlaves.add(idleSlave); 
				    	}

			    	    
			    	    System.out.println("RedistributingThread: Redistributed the jobs");			    	    	    	    
				    }//end if should redistribute or not
			    	else 
			    	{
			    		System.out.println("RedistributingThread: No need to redistribute");
			    	}
			    	
		    	}//end if working slaves or not	
	
		    } //end if idle slaves or not
		    	      
		    	
    	}//end while(true)
    	
    }//end run

}//end class



