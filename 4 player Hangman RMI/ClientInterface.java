package com.rmi.client;

/**
 * 
 * ClientInterface.java (Interface of Hangman Client)
 * 
 * Version 1.0
 * 
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is an interface for the RMI Hangman Client
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 * 
 *
 */

public interface ClientInterface extends Remote {

	/**
	 * This method is used to notify the client to start the play
	 * @param line: word for the hangman game
	 * @param playerId: player Id
	 * @throws RemoteException
	 */
	public void notifiedToPlay(String line, int playerId) throws RemoteException;
	
	/**
	 * This method is invoked to display winners on the screen
	 * @param winners: list of winners to display on the client
	 * @throws RemoteException
	 */
	public void displayWinners(String winners) throws RemoteException;
	
}
