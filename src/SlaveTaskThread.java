import java.io.*;
import java.util.LinkedList;

public class SlaveTaskThread extends Thread{

	private LinkedList<Job> tasks;
	private PrintWriter responseWriter; //to tell the slaveServerThread that it is done
    
    
	public SlaveTaskThread(LinkedList<Job> tasks, PrintWriter responseWriter)        
	{
		this.tasks = tasks;
		this.responseWriter = responseWriter;       
	}
	
	@Override
	public void run() 
	{
		System.out.println("SlaveTaskThread initialized"); //println for testing
		
		boolean empty = true;
		Job currTask = null;
		while(true)
		{
			empty = true;
			
			synchronized(tasks)
			{
				if(!tasks.isEmpty())
				{
					empty = false;
					currTask = tasks.removeFirst();					
				}
			}
			
			if(!empty)
			{	
				try {
					//perform task					
					int sleepTime = currTask.getDuration();
					System.out.println("SlaveTaskThread going to sleep for " + (sleepTime/1000) + " seconds");
					sleep(sleepTime);
					synchronized(tasks)
					{
						if(tasks.isEmpty()) 
						{
							 System.out.println("SlaveTaskThread: Done!");	                   
					         responseWriter.println("Done");	
						}
					}
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
			}				
		}
	}
}
