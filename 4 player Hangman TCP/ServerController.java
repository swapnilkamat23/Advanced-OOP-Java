/**
 * 
 * ServerControler.java (Controller for game server)
 * 
 * Version 2.0
 * 
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class acts as a controller for hangman game server
 * 
 * @author Swapnil Kamat (snk6855@rit.edu)
 * @author Siddharth Tarey (st2476@rit.edu)
 *
 */

public class ServerController implements Runnable {

	public Hangman player;
	public static String line = new String();
	//Sockets for 4 players
	static Socket sock1, sock2, sock3, sock4;

	// ArrayList of all the players
	public static ArrayList<Hangman> players = new ArrayList<Hangman>();

	public ServerController() {
	}

	/*
	 * Parameterized constructor, to initialize the the hangman player
	 */
	public ServerController(Hangman player) {
		this.player = player;
	}

	public static void main(String[] args) throws IOException {

		String filename = "C:/Users/swapnilkamat23/newWorkspace/Socket/src/words.txt";

		Random random = new Random();

		File textfile = new File(filename);

		boolean wordflag = true;

		Hangman h = new Hangman();

		ServerSocket servSock = new ServerSocket(2304);

		Thread t1 = new Thread();
		Thread t2 = new Thread();
		Thread t3 = new Thread();
		Thread t4 = new Thread();

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

		// this initializes players and starts play the game thread for each
		// player
		for (int index = 1; index <= 4; index++) {

			//Initializing player 1, adding to the arraylist of players and starting thread for player 1
			if (index == 1) {
				System.out.println("Waiting for player 1 to connect.. ");
				sock1 = servSock.accept();
				InputStream in1 = sock1.getInputStream();

				byte[] buffer = new byte[1024];

				int n = 0;

				String name = "";

				if ((n = in1.read(buffer)) != -1) {
					name = new String(buffer, 0, n);
				}
				players.add(new Hangman(name, 0, sock1, false));
				t1 = new Thread(new ServerController(players.get(0)));
				t1.start();
			}
			//Initializing player 2, adding to the arraylist of players and starting thread for player 2
			else if (index == 2) {
				System.out.println("Waiting for player 2 to connect.. ");
				sock2 = servSock.accept();
				InputStream in2 = sock2.getInputStream();

				byte[] buffer = new byte[1024];

				int n = 0;

				String name = "";

				if ((n = in2.read(buffer)) != -1) {
					name = new String(buffer, 0, n);
				}
				players.add(new Hangman(name, 0, sock2, false));
				t2 = new Thread(new ServerController(players.get(1)));
				t2.start();
			}
			//Initializing player 3, adding to the arraylist of players and starting thread for player 3
			else if (index == 3) {
				System.out.println("Waiting for player 3 to connect.. ");
				sock3 = servSock.accept();
				InputStream in3 = sock3.getInputStream();

				byte[] buffer = new byte[1024];

				int n = 0;

				String name = "";

				if ((n = in3.read(buffer)) != -1) {
					name = new String(buffer, 0, n);
				}
				players.add(new Hangman(name, 0, sock3, false));
				t3 = new Thread(new ServerController(players.get(2)));
				t3.start();
			}
			//Initializing player 4, adding to the arraylist of players and starting thread for player 4
			else if (index == 4) {
				System.out.println("Waiting for player 4 to connect.. ");
				sock4 = servSock.accept();
				InputStream in4 = sock4.getInputStream();

				byte[] buffer = new byte[1024];

				int n = 0;

				String name = "";

				if ((n = in4.read(buffer)) != -1) {
					name = new String(buffer, 0, n);
				}
				players.add(new Hangman(name, 0, sock4, false));
				t4 = new Thread(new ServerController(players.get(3)));
				t4.start();
			}
		}

		while (t1.isAlive() == true || t2.isAlive() == true || t3.isAlive() == true || t4.isAlive() == true) {
		}

		
		//Determine the winner and write out to the clients
		String winner = h.displayWinner(players);

		OutputStream os1 = sock1.getOutputStream();
		OutputStream os2 = sock2.getOutputStream();
		OutputStream os3 = sock3.getOutputStream();
		OutputStream os4 = sock4.getOutputStream();
		os1.write(winner.getBytes());
		os2.write(winner.getBytes());
		os3.write(winner.getBytes());
		os4.write(winner.getBytes());

		os1.close();
		os2.close();
		os3.close();
		os4.close();
	}

	/**
	 * This function starts the Hangman game for each individual player
	 * 
	 * @param line:
	 *            the word that has to be guessed by the player
	 * @param player:
	 *            the player that is currently playing the game
	 * @throws IOException
	 */
	public void play(String line, Hangman player) throws IOException {

		OutputStream os = this.player.sock.getOutputStream();
		
		System.out.println(this.player.name + " connected to server");

		os.write("Connected to server\n".getBytes());

		char[] word = new char[line.length()];

		word = line.toCharArray();

		boolean[] check = new boolean[line.length()];

		int guess = 0;

		char[] miss = new char[8];

		Hangman play = new Hangman();

		do {
			String displayWord = play.displayword(word, check);
			os.write(("\n" + displayWord).getBytes());
			os.flush();
			// Prints the letters that have been guessed wrong
			String printMisses = play.printMisses(miss);
			os.write(printMisses.getBytes());
			os.flush();
			
			os.write("\nGuess: ".getBytes());
			os.flush();

			InputStream in1 = this.player.sock.getInputStream();

			byte[] buffer = new byte[1024];

			int n = 0;

			String charGuess = "";
			// Takes the input of letter from the player
			if ((n = in1.read(buffer)) != -1) {
				charGuess = new String(buffer, 0, n);
			}

			char input = charGuess.charAt(0);

			Character ch1 = new Character(input);

			String mystring = ch1.toString().toLowerCase();

			// if the character entered by the user is a alphabet, the condition
			// will be true
			if (play.checkcharacters(mystring)) {

				input = mystring.toCharArray()[0];
				// if the player makes a correct guess 10 points are added
				if (play.matchletter(word, check, input, player) == 1) {

					player.points += 10;
				}
				// if letter has already been guessed, print the below message
				else if (play.matchletter(word, check, input, player) == 2) {

					os.write("This letter has already been played\n".getBytes());
					os.flush();
				}

				// if the guess is wrong, execute the else statement.
				else {

					// if the letter has already been missed by the user, print
					// the below message
					if (!play.checkMisses(miss, input)) {

						os.write("This letter has already been played\n".getBytes());
						os.flush();
					}

					// deduct 5 points if the above condition is false.
					else {
						player.points -= 5;
						miss[guess] = input;
						guess += 1;
					}

				}
				os.write(("Score: " + player.points + "\n").getBytes());
				os.flush();
			}

			else {
				os.write("Enter only lower case alphabets\n".getBytes());
				os.flush();
				continue;
			}
		} while (guess <= 7 && !play.checkComplete(check, this.player));
		
		os.write("\nWaiting for other players to finish their play..\nWinners will be announced soon..\n".getBytes());
	}

	@Override
	public void run() {

		try {
			//starts the play for each player
			this.play(line, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
