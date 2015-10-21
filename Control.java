//The main method and control of neural net creation and operation

import java.util.Scanner;

public class Control {

	private final static int DEFAULT_BOARD_SIZE = 15;
	private final static int DEFAULT_SIGHT_RANGE = 5; //the side dimensions of the visible square
	private final static int[] DEFAULT_HIDDEN_LAYER_SIZES = {100}; //array of hidden layer sizes
	private final static double PASSIVE_PUNISHMENT = 0.8; //the severity with which the network is encouraged to find gold (lower is more severe)
	
	private static Scanner scan = new Scanner(System.in);
	private static NNetwork network; // the neural network being used (assumes only using one at a time)

	public static void main(String[] args) {
		
		network = new NNetwork(2 * (int)(Math.pow(DEFAULT_SIGHT_RANGE, 2)), DEFAULT_HIDDEN_LAYER_SIZES, 5);
		System.out.println("How many times should the agent play?");
		int rounds = scan.nextInt();
		int record = 0; //the longest the computer has lasted
		for(int i = 0; i < rounds; i++) {
			//System.out.println("GAME " + i);
			Board b = new Board(DEFAULT_BOARD_SIZE);
			int gold = 0;
			
			//while the player has neither died nor found all the gold
			while(b.getPlayerStatus() && !b.foundAllGold()) {
				
				Tile[][] v = b.visible((DEFAULT_SIGHT_RANGE - 1) / 2); //side -> distance from middle
				
				//plug the neural network into the game
				double[] inputs = new double[(2 * (int)(Math.pow(DEFAULT_SIGHT_RANGE, 2)))];
				for(int j = 0; j < 25; j++) {
					//add monsters to input
					if(v[j / 5][j % 5].getContent().equals("monster"))
						inputs[j] = 1.0;
					else
						inputs[j] = 0.0;
					//add gold to input
					if(v[j / 5][j % 5].getContent().equals("gold"))
						inputs[j + 25] = 1.0;
					else
						inputs[j + 25] = 0.0;
				}
								
				double[][] outputs = network.fullEvaluate(inputs); //network evaluates the game state
				double[] outs = outputs[outputs.length - 1]; //take just the output layer
				
				//TODO remove
				/*for(int j = 0; j < outs.length; j++) {
					System.out.println("    Output at " + j + ": " + outs[j]);
				}
				System.out.println("\n");*/
				
				int dir = getGreatestIndex(outs); //find the direction the network picked
				b.movePlayer(dir); //move in the direction the network picked
				b.step(); //advance the rest of the board (monsters move)
				
				double[] trainer = new double[5]; //the values to train the network with
				//If the player dies, correct it to not move that way				
				if(!b.getPlayerStatus()) {
					//System.out.println("Died on turn " + b.getTurn() + " with " + b.getGoldCount() + " gold.");
					for(int j = 0; j < 5; j++) {
						if(j == dir) {
							trainer[j] = 0.0;
						}
						else {
							trainer[j] = outs[j];
						}
					}
				}
				//reward network for finding gold
				else if(b.getGoldCount() > gold) {
					if(b.foundAllGold())
						System.out.println("COMPLETED GAME: Computer found all gold in round " + i + " in " + b.getTurn() + " turns.");
					gold = b.getGoldCount();
					for(int j = 0; j < 5; j++) {
						if(j == dir) {
							trainer[j] = 1.0;
						}
						else {
							trainer[j] = outs[j];
						}
					}
				}
				//poke the network for not accomplishing anything
				else {
					for(int j = 0; j < 5; j++) {
						if(j == dir) {
							trainer[j] = outs[j] * PASSIVE_PUNISHMENT;
						}
						else {
							trainer[j] = outs[j];
						}
					}
				}				
				network.backProp(outputs, trainer); //train the network with the backpropagation algorithm
			}
			
			if(b.getTurn() > record) {
				record = b.getTurn();
				System.out.println("NEW RECORD: Computer lasted " + record + " turns in round " + i + " and found " + gold + " gold.");
			}
		}
	}

	
	//finds the position in an array with the highest value
	private static int getGreatestIndex(double[] d) {
		double max = d[0];
		int maxIndex = 0;
		for(int i = 1; i < d.length; i++) {
			if(d[i] > max) {
				max = d[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}
