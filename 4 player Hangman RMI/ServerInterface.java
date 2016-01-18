package com.rmi.server;

/**
 * 
 * ServerInterface.java (Interface of Hangman Server)
 * 
 * Version 1.0
 * 
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.client.ClientInterface;

/**
 * This is an interface for the RMI Hangman Server
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 * 
 *
 */

public interface ServerInterface extends Remote {

	/**
	 * Registers client to the server
	 * @param client
	 * @param name
	 * @throws RemoteException
	 */
	public void registerPlayer(ClientInterface client, String name) throws RemoteException;
	
	/**
	 * Displays the hangman word during the play
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @return
	 * @throws RemoteException
	 */
	public String displayWord(char[] word, boolean[] check) throws RemoteException;
	
	/**
	 * prints the misses played by the player
	 * @param miss: characters that were a miss
	 * @return
	 * @throws RemoteException
	 */
	public String printMisses(char[] miss) throws RemoteException;
	
	/**
	 * check input characters if its a legal input
	 * @param mystring: input string
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkcharacters(String mystring) throws RemoteException;
	
	/**
	 * check if the input letter is in the hangman word to be guessed
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @param input: input char
	 * @param playerId: player id
	 * @return
	 * @throws RemoteException
	 */
	public int matchletter(char[] word, boolean[] check, char input, int playerId) throws RemoteException;
	
	/**
	 * update points of the player
	 * @param playerId: player id
	 * @param operation: 'add' or 'subtract'
	 * @param points: number of points to add or subtract
	 * @throws RemoteException
	 */
	public void updatePlayerPoints(int playerId, String operation, int points) throws RemoteException;
	
	/**
	 * check if the character input is already in the misses array
	 * @param miss: character array of misses
	 * @param input: input character
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkMisses(char[] miss, char input) throws RemoteException;
	
	/**
	 * get points of the player
	 * @param playerId: player id
	 * @return points of the player requested
	 * @throws RemoteException
	 */
	public int getPlayerPoints(int playerId) throws RemoteException;
	
	/**
	 * checks if the player has finished guessing the word or the tries have exceeded the limit
	 * @param check: boolean check array
	 * @param playerId: player id
	 * @return: true if the game is completed
	 * @throws RemoteException
	 */
	public boolean checkComplete(boolean[] check, int playerId) throws RemoteException;
	
	/**
	 * update the boolean check array
	 * @param word: character array of hangman word
	 * @param check: boolean check array
	 * @param input: char input
	 * @param playerId: player id
	 * @return: 
	 * @throws RemoteException
	 */
	public boolean[] updateCheck(char[] word, boolean[] check, char input, int playerId) throws RemoteException;
	
	/**
	 * displays the winners
	 * @throws RemoteException
	 */
	public void displayWinners() throws RemoteException;
}
