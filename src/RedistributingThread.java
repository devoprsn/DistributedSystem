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
    	
    	SlaveServerThread maxWorkSlave;   //slave with the most time left to complete its tasks
    	int totalDuration, maxTotalDuration;  //time left for each slave to complete all their tasks
    	int numTasksLeft;              //number of tasks the slave with the longest total duration has left
    	int jobsToRedistribute;        //number of tasks to redistribute
    	SlaveServerThread idleSlave;   //slave that jobs will be redistributed to
    	
    	while(true)
    	{
		    	try {
					sleep(50000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	if(!idleSlaves.isEmpty())  // if no idle slaves, don't redistribute
		    	{
		    		System.out.println("RedistributingThread: idle slave (s) found");
		    		
				    	maxWorkSlave = workingSlaves.get(0);  
				    	
				    	maxTotalDuration = maxWorkSlave.getTotalDurationOfAllTasks(); //request slaveSlaveServerThread to send its total duration
				    	
				    	for(int i = 1; i < workingSlaves.size(); i++)
				    	{
				    		totalDuration = workingSlaves.get(i).getTotalDurationOfAllTasks();
				    		if(totalDuration > maxTotalDuration)
				    		{
				    			maxTotalDuration = totalDuration;
				    			maxWorkSlave = workingSlaves.get(i);
				    		}
				    	}
				    	
				    	numTasksLeft = maxWorkSlave.getCountOfTasks();
				    	
				    	if (numTasksLeft>=3) 
				    	{
				    		//so redistribute:
				    		
				    		System.out.println("RedistributingThread: need to redistribute");
				    		
				    		if (numTasksLeft%2 ==0)  //even amount
				    		{
				    			jobsToRedistribute = numTasksLeft/2;
				    		}
				    		else  //odd amount
				    		{
				    			jobsToRedistribute = (numTasksLeft-1)/2;
				    		}
				    		
				    	    
				    	    
				    	    synchronized(idleSlaves)
				    	    {
				    	    	idleSlave = idleSlaves.getFirst();
				    	    }
				    	    
				    	    int i = 0;
				    	    
				    	    while(i<jobsToRedistribute)
				    	    {
				    	    	int duration = maxWorkSlave.removeJob(); //remove job and get the duration of the job being removed
				    	    	
				    	    	idleSlave.addJobWithDuration(duration);  // add the job with the duration to the idle slave
				    	    }
				    	    	
				    		
				    	    
				    	    //synchronized()
				    	    //{
				    	    	idleSlaves.remove(idleSlave);
				    	    	workingSlaves.add(idleSlave);
				    	    //}
				    	}
				    	
	
		    	}
		    	      System.out.println("RedistributingThread: redistributed the jobs");
		    	
    	}
    	
    	
	}

}



