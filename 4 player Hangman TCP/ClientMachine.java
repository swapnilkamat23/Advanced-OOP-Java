/**
 * 
 * ClientMachine.java (client module for the hangman game)
 * 
 * Version 1.0
 * 
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * This class acts as a client module for the hangman multiplayer game
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 * 
 *
 */

public class ClientMachine {

	public static void main(String[] args) throws Exception {

		ClientMachine client = new ClientMachine();
		client.start();

	}

	public void start() throws Exception {
		try {
			Socket sock = new Socket("localhost", 2304);

			System.out.print("Enter your name: ");
			
			Scanner inp = new Scanner(System.in);
			
			String Name = inp.nextLine();
			
			OutputStream os = sock.getOutputStream();
			
			os.write(Name.getBytes());
			
			os.flush();
			
			InputStream in = sock.getInputStream();
			
			byte[] buffer = new byte[1024];
			
			int n=0;
			
			String incoming = "";
			
			while((n=in.read(buffer))!=-1)
			{
				incoming = new String(buffer,0,n);
				
				if(incoming.contains("Guess: ")){
					System.out.print(incoming);
					Scanner getGuess = new Scanner(System.in);
					String s = getGuess.nextLine();
					os.write(s.getBytes());
					os.flush();
				}
				else
				{
					System.out.println(incoming);
				}
					
			}
			
		} catch (Exception e) {

		}
	}

}
