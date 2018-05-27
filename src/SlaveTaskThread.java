
import java.io.PrintWriter;
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
		
		while(true)
		{
			while(tasks.isEmpty());
			try {
				//perform task
				sleep(tasks.getFirst().getDuration());
				
				//remove task from task list				
				tasks.removeFirst();
			
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		
			//no tasks left, notify slaveServerThread			
			responseWriter.println("Done");		
		}
	}
}
