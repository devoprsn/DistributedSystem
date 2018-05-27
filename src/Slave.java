import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

//here the slave has a serverSocket that communicates with its SlaveServerThread's clientSocket

public class Slave {

	private static IRandomValueGenerator rand;
	
	public Slave(IRandomValueGenerator rand)
	{
		Slave.rand = rand;
	}
	
	public static void main(String[] args)
	{
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(portNumber);
		}
		catch(IOException e)
		{
		        System.out.println(e.getMessage());
		}
		System.out.println("Slave initialized"); //println for testing	
		LinkedList<Job> tasks = new LinkedList<Job>();
		
		
		try (Socket clientSocket = serverSocket.accept();
					PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) 
		{
			String jobRequest;
			SlaveTaskThread taskThread = new SlaveTaskThread(tasks,responseWriter);
			taskThread.start();
			
			while ((jobRequest = requestReader.readLine()) != null)
			{				
				Job job = new Job(rand);
				System.out.println(jobRequest);
				
				tasks.add(job);	
				
				
			}
			
			taskThread.join();
		} 
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + serverSocket.getLocalPort() + " or listening for a connection");
			System.out.println(e.getMessage());
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}		
		
	}
}
