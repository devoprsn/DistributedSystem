import java.util.LinkedList;

public class SlaveTaskThread extends Thread{

	LinkedList<Job> tasks;
	public SlaveTaskThread(LinkedList<Job> tasks)
	{
		this.tasks = tasks;
	}
	
	@Override
	public void run() 
	{
		while(tasks.size() != 0)
		{
			try {
				sleep(tasks.getFirst().getDuration());
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
