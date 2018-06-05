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
    	SlaveServerThread idleSlave;   //slave that jobs will be redistributed to
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
		    	
		    	synchronized(workingSlaves)
		    	{
		    		if(!workingSlaves.isEmpty())
		    		{
		    			emptyWorkingSlaves = false;
		    		}
		    	}
		    	
		    	if(!emptyIdleSlaves)  //only redistribute if there is one or more idle slaves
		    	{
		    		System.out.println("RedistributingThread: idle slave (s) found");
		    		if(!emptyWorkingSlaves)
		    		{
		    			System.out.println("RedistributingThread: working slave (s) found");
		    			
		    		    synchronized(workingSlaves)
		    		    {
		    		    	maxWorkSlave = workingSlaves.get(0);  
		    		    }
				    	
				    	maxTotalDuration = maxWorkSlave.getTotalDurationOfAllTasks(); //request slaveSlaveServerThread to send its total duration
				    	
				    	synchronized(workingSlaves)  //synchronized on whole loop so size of workingSlaves shouldn't change mid-loop
				    	{
				    		size = workingSlaves.size();	    					    	
				    	}
				    	
				    	SlaveServerThread slave;
				    	for(int i = 1; i < size; i++)
				    	{		
				    		synchronized(workingSlaves)
				    		{
				    			slave = workingSlaves.get(i);
				    		}
				    		totalDuration = slave.getTotalDurationOfAllTasks();
				    		if(totalDuration > maxTotalDuration)
				    		{
				    			maxTotalDuration = totalDuration;
				    			maxWorkSlave = slave;
				    		}
				    	}
				    					    	
				    	numTasksLeft = maxWorkSlave.getCountOfTasks();
				    	
				    	if (numTasksLeft>=3) 
				    	{
				    		//so redistribute:				    		
				    		System.out.println("RedistributingThread: need to redistribute");
				    		
				    		if (numTasksLeft%2 == 0)  //even amount
				    		{
				    			jobsToRedistribute = numTasksLeft/2;
				    		}
				    		else  //odd amount
				    		{
				    			jobsToRedistribute = (numTasksLeft-1)/2;
				    		}		    		
				    	    
				    	    
				    	    synchronized(idleSlaves)
				    	    {
				    	    	idleSlave = idleSlaves.removeFirst();
				    	    }
				    	    
				    	    int i = 0;
				    	    
				    	    while(i<jobsToRedistribute)
				    	    {
				    	    	int duration = maxWorkSlave.removeJob(); //remove job and get the duration of the job being removed				    	    	
				    	    	idleSlave.addJobWithDuration(duration);  // add the job with the duration to the idle slave
				    	    }
				    	    	
				    	    
				    	    //now gives the job of removing and adding slaves to the slaveThreadedServer
				    	    idleSlave.getMyThreadedServerBoss().idleToWorking(idleSlave);
				    	    
				    	    System.out.println("RedistributingThread: redistributed the jobs");
				    	    	    	    
				    	}//end if should redistribute or not
				    	
		    		}//end if working slaves or not	
	
		    	} //end if idle slaves or not
		    	      
		    	
    	}//end while(true)
    	
    	
	}//end run()

}//end class



