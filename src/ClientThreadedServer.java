
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClientThreadedServer implements Runnable{
	
	private int portNumber;
	private LinkedList<String> jobs;
	
	public ClientThreadedServer(int portNumber, LinkedList<String> jobs)
	{
		this.portNumber = portNumber;
		this.jobs = jobs;
	}
	
	@Override
	public void run() 
	{		
		
		final int THREADS = 3;	
		
		System.out.println("ClientThreadedServer's run() method has begun!");
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber);) 
		{
			System.out.println("ServerSocket set up in try");
			
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for (int i = 0; i < THREADS; i++)
				threads.add(new Thread(new ClientServerThread(serverSocket, i, jobs)));
			
			
			for (Thread t : threads)
				t.start();
			
			System.out.println("Each clientServerThread is started!");
			
			
			for (Thread t: threads)
			{
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} 
		
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber+ " or listening for a connection");
			System.out.println(e.getMessage());
		}
		
	}

}
			
			
		
