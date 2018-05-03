import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.net.UnknownHostException;

public class SlaveServerThread implements Runnable{

	String IPAddress;
	public SlaveServerThread(String IPAddress)
	{
		this.IPAddress = IPAddress;
	}

	@Override
	public void run() 
	{
		try (Socket slaveSocket = new Socket(IPAddress, 4501);
			ObjectOutputStream outputStream = new ObjectOutputStream(slaveSocket.getOutputStream());
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()))
				) {
			Job curr;
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{			
			e.printStackTrace();
		}
	}
}
