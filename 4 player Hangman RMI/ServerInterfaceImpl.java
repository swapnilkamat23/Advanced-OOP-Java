package com.rmi.server;

/**
 * 
 * ServerInterfaceImpl.java (Hangman Server)
 * 
 * Version 1.0
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.rmi.client.ClientInterface;

/**
 * RMI Hangman Server
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 * 
 *
 */

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {

	private static final long serialVersionUID = 1L;

	private static ArrayList<ClientInterface> clients;

	public static String line = new String();

	private static ArrayList<Hangman> players;

	private static Hangman h = new Hangman();

	private static int declareWinners = 0;

	protected ServerInterfaceImpl() throws RemoteException {
		clients = new ArrayList<ClientInterface>();
		players = new ArrayList<Hangman>();
	}

	/**
	 * Registers client to the server
	 * @param client
	 * @param name
	 * @throws RemoteException
	 */
	public void registerPlayer(ClientInterface client, String name) throws RemoteException {
		clients.add(client);
		players.add(new Hangman(name, 0, false));
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {

		ServerInterfaceImpl s = new ServerInterfaceImpl();

		Naming.rebind("ServerInterface", s);

		System.out.println("waiting for 4 players to connect...");

		while (true) {
			if (players.size() == 4) {
				for (Hangman p : players) {
					System.out.println(p.name + " connected to the server");
				}
				break;
			} else {
				System.out.print("");
			}
		}

		System.out.println("4 players connected.. Game begins!");

		String filename = "C:/Users/swapnilkamat23/newWorkspace/RMIHangman/src/com/rmi/server/words.txt";

		Random random = new Random();

		File textfile = new File(filename);

		boolean wordflag = true;

		try {
			Scanner in = new Scanner(textfile);

			// this loop executes till a correct word(without special
			// characters) is selected.
			while (wordflag == true) {
				// randomly selects an integer
				int select = random.nextInt(3);
				// selects a word based on the random integer.
				for (int index = 0; index <= select; index++) {
					line = in.nextLine();
					line = line.toLowerCase();
				}
				if (h.checkcharacters(line)) {
					wordflag = false;
				}
			}
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			System.exit(0);
		}

		int i = 0;
		while (i < clients.size()) {
			clients.get(i).notifiedToPlay(line, i);
			i++;
		}

		int j = 0;
		
		while (declareWinners < 4) {
			System.out.print("");
			if (declareWinners == 4) {
				while (j < clients.size()) {
					System.out.print("");
					clients.get(j).displayWinners(h.displayWinner(players));
					j++;
				}
				break;
			}
		}
		
		System.out.println("Game ended.");
		System.out.println(h.displayWinner(players));
	}

	@Override
	/**
	 * Displays the hangman word during the play
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @return
	 * @throws RemoteException
	 */
	public String displayWord(char[] word, boolean[] check) throws RemoteException {

		return h.displayWord(word, check);
	}

	@Override
	/**
	 * prints the misses played by the player
	 * @param miss: characters that were a miss
	 * @return
	 * @throws RemoteException
	 */
	public String printMisses(char[] miss) throws RemoteException {
		return h.printMisses(miss);
	}

	@Override
	/**
	 * check input characters if its a legal input
	 * @param mystring: input string
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkcharacters(String mystring) throws RemoteException {
		return h.checkcharacters(mystring);
	}

	@Override
	/**
	 * check if the input letter is in the hangman word to be guessed
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @param input: input char
	 * @param playerId: player id
	 * @return
	 * @throws RemoteException
	 */
	public int matchletter(char[] word, boolean[] check, char input, int playerId) throws RemoteException {
		return h.matchletter(word, check, input, players.get(playerId));
	}

	@Override
	/**
	 * update points of the player
	 * @param playerId: player id
	 * @param operation: 'add' or 'subtract'
	 * @param points: number of points to add or subtract
	 * @throws RemoteException
	 */
	public void updatePlayerPoints(int playerId, String operation, int points) throws RemoteException {
		h.updatePlayerPoints(players.get(playerId), operation, points);
	}

	@Override
	/**
	 * check if the character input is already in the misses array
	 * @param miss: character array of misses
	 * @param input: input character
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkMisses(char[] miss, char input) throws RemoteException {
		return h.checkMisses(miss, input);
	}

	@Override
	/**
	 * get points of the player
	 * @param playerId: player id
	 * @return points of the player requested
	 * @throws RemoteException
	 */
	public int getPlayerPoints(int playerId) throws RemoteException {
		return players.get(playerId).points;
	}

	@Override
	/**
	 * checks if the player has finished guessing the word or the tries have exceeded the limit
	 * @param check: boolean check array
	 * @param playerId: player id
	 * @return: true if the game is completed
	 * @throws RemoteException
	 */
	public boolean checkComplete(boolean[] check, int playerId) throws RemoteException {
		return h.checkComplete(check, players.get(playerId));
	}

	@Override
	/**
	 * update the boolean check array
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @param input: char input
	 * @param playerId: player id
	 * @return: 
	 * @throws RemoteException
	 */
	public boolean[] updateCheck(char[] word, boolean[] check, char input, int playerId) throws RemoteException {
		return h.updateCheck(word, check, input, players.get(playerId));
	}

	@Override
	/**
	 * displays the winners
	 * @throws RemoteException
	 */
	public void displayWinners() {
		declareWinners += 1;
	}

}
