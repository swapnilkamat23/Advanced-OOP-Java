package com.rmi.client;

/**
 * 
 * ClientInterfaceImpl2.java (Hangman Client)
 * 
 * Version 1.0
 * 
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import com.rmi.server.ServerInterface;

/**
 * RMI Hangman Client
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 * 
 *
 */

public class ClientInterfaceImpl2 extends UnicastRemoteObject implements ClientInterface  {
	
	private static final long serialVersionUID = 1L;
	public static ServerInterface si;
	static String name; 
	static int playerId;
	static boolean notified = false;
	static String line;

	public ClientInterfaceImpl2(String name) throws RemoteException {
		this.name = name;
		si.registerPlayer(this,this.name);
	}

	public static void main(String[] args) throws NotBoundException, IOException {
		
		
		String url = "rmi://localhost/ServerInterface";
		si = (ServerInterface)Naming.lookup(url);
		System.out.print("Enter player name: ");
		Scanner in = new Scanner(System.in);
		String nameOfPlayer = in.nextLine();
		ClientInterfaceImpl2 cii = new ClientInterfaceImpl2(nameOfPlayer);
		System.out.println("\nHoorray! You are connected to the server.\nWaiting for other players to connect.. ");
		
		while(!notified){
			System.out.print("");
			if(notified)
			break;
		}
		
		System.out.println();
		cii.play(line);
		
		si.displayWinners();
	}
	
	public static boolean[] check;
	public static char[] word;
	public static char[] miss;
	
	public void play(String line) throws IOException {
		
		word = new char[line.length()];

		word = line.toCharArray();

		check = new boolean[line.length()];

		int guess = 0;

		miss = new char[8];

		do {
			String displayWord = si.displayWord(word, check);
			System.out.println(displayWord);
			
			// Prints the letters that have been guessed wrong
			String printMisses = si.printMisses(miss);
			System.out.println(printMisses);

			System.out.print("Guess: ");
			String charGuess = "";
			Scanner in = new Scanner(System.in);
			charGuess = in.nextLine();

			char input = charGuess.charAt(0);

			Character ch1 = new Character(input);

			String mystring = ch1.toString().toLowerCase();

			// if the character entered by the user is a alphabet, the condition
			// will be true
			if (si.checkcharacters(mystring)) {

				input = mystring.toCharArray()[0];
				// if the player makes a correct guess 10 points are added
				if (si.matchletter(word, check, input, this.playerId) == 1) {
					check = si.updateCheck(word, check, input, this.playerId);
					si.updatePlayerPoints(this.playerId, "add", 10);
					
				}
				// if letter has already been guessed, print the below message
				else if (si.matchletter(word, check, input, this.playerId) == 2) {
					System.out.println("This letter has already been played\n");
				}

				// if the guess is wrong, execute the else statement.
				else {

					// if the letter has already been missed by the user, print
					// the below message
					if (!si.checkMisses(miss, input)) {

						System.out.println("This letter has already been played\n");
					}

					// deduct 5 points if the above condition is false.
					else {
						si.updatePlayerPoints(this.playerId, "subtract", 5);
						miss[guess] = input;
						guess += 1;
					}

				}
				System.out.println("Score: " + si.getPlayerPoints(this.playerId) + "\n");
			}

			else {
				System.out.println("Enter only lower case alphabets\n");
				continue;
			}
		} while (guess <= 7 && !si.checkComplete(check, this.playerId));
		
		System.out.println("Waiting for other players to finish their play..\nWinners will be announced soon..\n");
		
	}
	

	@Override
	/**
	 * This method is used to notify the client to start the play
	 * @param line: word for the hangman game
	 * @param playerId: player Id
	 * @throws RemoteException
	 */
	public void notifiedToPlay(String line, int playerId) throws RemoteException {
		this.playerId = playerId;
		this.line = line;
		this.notified = true;
	}
	
	@Override
	/**
	 * This method is invoked to display winners on the screen
	 * @param winners: list of winners to display on the client
	 * @throws RemoteException
	 */
	public void displayWinners(String winners) throws RemoteException {
		System.out.println(winners);
	}
}
