import java.util.LinkedList;

public class SlaveThreadedServer implements Runnable
{
	String[] IPAddresses;
	LinkedList<String> jobs;
	LinkedList idleSlaves;
	LinkedList workingSlaves;
   public SlaveThreadedServer(LinkedList<String> jobs)
   {
	  this.jobs = jobs;
   }

	@Override
	public void run()
	{
		
			
	}
}
