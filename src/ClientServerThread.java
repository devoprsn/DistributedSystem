import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ClientServerThread extends Thread {
	
	private ServerSocket serverSocket = null;
	int id; 
	LinkedList<String> jobs;
	
	public ClientServerThread(ServerSocket s, int id, LinkedList<String> jobs)
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

			System.out.println("ClientServerThread "+id+"run() has begun!"); //println for testing

			
			String requestString;
			while ((requestString = requestReader.readLine()) != null) 
			{				
				responseWriter.println("Request has been received by thread " + id);
				String job = requestString;
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
