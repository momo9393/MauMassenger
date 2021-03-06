package MAU_new_Massenger;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class ClientTest1 {
	ObjectInputStream ois;
	Socket socket;
	ObjectOutputStream ous;
	private Message msg;
	private CommandHandler commandHandler;
	private ArrayList<String> onlineUser=new ArrayList<String>();

	User mohammed=new User("Mohammed");
	User hazem=new User("Hazem");
	User us3=new User("Ali");
	public void statrClient() throws IOException {
		socket = new Socket(InetAddress.getLocalHost(), 9020);
		ous = new ObjectOutputStream(socket.getOutputStream());
		//
		ImageIcon imageIcon=new ImageIcon("files/orkanen.jpg");

		ous.writeObject(mohammed);

		Message message=new Message(mohammed,hazem,"Hello Hazem this is Mohammed!");





		Message mess=new Message(mohammed,hazem,"Here Comes yet another message");
		// ous.writeObject((Object)mess);

		ous.flush();
		//new SendMsg().start();
		new Connect().start();
	}
	public User getUser()
	{
		return mohammed;
	}
	public class SendMsg extends Thread{
		public void run()
		{
			try {
				while (true)
				{	
					String txt=JOptionPane.showInputDialog(null,"Enter your text");
					Message mes=new Message(mohammed,hazem,txt);
					ous.writeObject((Object)mes);

				}
			}
			catch(IOException e)
			{
				System.out.print(e);
			}
		}

	}
	
	
	public class Connect extends Thread {
		@Override
		public void run(){
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("WE GOT SOMETHING");
				Object us=new Object();
				
				while (true) {
				
					us = ois.readObject();
					if(us instanceof CommandHandler)//////////// ADDED
						commandHandler(us);
					else if(us instanceof Message)
					{
						msg=(Message)us;
						System.out.println(msg.getTxt());
					}

				}
			} catch (IOException | ClassNotFoundException e) {
					
				System.out.println("WE HAVE A REAL PROBLEM" + e);
			}

		}
	}
	private void commandHandler(Object us) { ///////////////////////ADDED
		commandHandler=(CommandHandler) us;
		String command=commandHandler.getCommand();
		if(command.equals("add"))
		{
			onlineUser.add(commandHandler.getUser().getName());
			
		}
		else
		{
			
			onlineUser.remove(commandHandler.getUser().getName());
		}
		System.out.println("Those who are online:");
		for(int x=0;x<onlineUser.size();x++)
		{
			
			System.out.println(onlineUser.get(x));
		}
		
		
	}

	public static void main(String[] args) throws IOException {
		ClientTest1 clientTest = new ClientTest1();
		clientTest.statrClient();
	}
}
