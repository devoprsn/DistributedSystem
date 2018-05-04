import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ClientServerThread implements Runnable {
	
	private ServerSocket serverSocket = null;
	int id; 
	LinkedList<Job> jobs;
	public ClientServerThread(ServerSocket s, int id, LinkedList<Job> jobs)
	{
		serverSocket = s;
		this.id = id;
		this.jobs = jobs;
	}
	
	@Override
	public void run() 
	{
		try (Socket clientSocket = serverSocket.accept();
				PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) 
		{
			String requestString;
			while ((requestString = requestReader.readLine()) != null) 
			{				
				responseWriter.println("Request has been received by ");
				Job job = new Job(new RandomValueGenerator());
				synchronized(jobs)
				{
					jobs.add(job);
				}
			}
		} 
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + serverSocket.getLocalPort() + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
