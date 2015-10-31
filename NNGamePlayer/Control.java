//The main method and control of neural net creation and operation

import java.util.Arrays;
import java.util.Scanner;

public class Control {

	private final static int DEFAULT_BOARD_SIZE = 15;
	private final static int DEFAULT_SIGHT_RANGE = 5; //the side dimensions of the visible square
	private final static int[] DEFAULT_HIDDEN_LAYER_SIZES = {200}; //array of hidden layer sizes
	private final static double PASSIVE_PUNISHMENT = 0.8; //the severity with which the network is encouraged to find gold (lower is more severe)
	private final static double MONSTER_FEAR_VALUE = 0.05;
	
	private static double fearLevel = 0.0;
	private static Scanner scan = new Scanner(System.in);
	private static NNetwork network; // the neural network being used (assumes only using one at a time)

	public static void main(String[] args) {
		
		network = new NNetwork(3 * (int)(Math.pow(DEFAULT_SIGHT_RANGE, 2)), DEFAULT_HIDDEN_LAYER_SIZES, 5);
		System.out.println("How many times should the agent play?");
		int rounds = scan.nextInt();
		int record = 0; //the longest the computer has lasted
		int goldRecord = 0; //the most gold the computer has found
		for(int i = 0; i < rounds; i++) {
			//System.out.println("GAME " + i);
			Board b = new Board(DEFAULT_BOARD_SIZE);
			int gold = 0;
			
			//while the player has neither died nor found all the gold
			while(b.getPlayerStatus() /*&& !b.foundAllGold()*/) {
				
				Tile[][] v = b.visible((DEFAULT_SIGHT_RANGE - 1) / 2); //side -> distance from middle
				//count monsters
				fearLevel = 0;
				for(int j = 0; j < v.length; j++) {
					for(int k = 0; k < v[j].length; k++) {
						if(v[j][k].getContent().equals("monster"))
							fearLevel += MONSTER_FEAR_VALUE;
					}
				}
				boolean[][] bread = b.getBreadCrumbs(DEFAULT_SIGHT_RANGE);
				
				//plug the neural network into the game
				int square = (int)(Math.pow(DEFAULT_SIGHT_RANGE, 2));
				double[] inputs = new double[3 * square];
				for(int j = 0; j < square; j++) {
					//add monsters to input
					if(v[j / DEFAULT_SIGHT_RANGE][j % DEFAULT_SIGHT_RANGE].getContent().equals("monster"))
						inputs[j] = 1.0;
					else
						inputs[j] = 0.0;
					//add gold to input
					if(v[j / DEFAULT_SIGHT_RANGE][j % DEFAULT_SIGHT_RANGE].getContent().equals("gold"))
						inputs[j + square] = 1.0;
					else
						inputs[j + square] = 0.0;
					//add breadcrumbs to input
					if(bread[j / DEFAULT_SIGHT_RANGE][j % DEFAULT_SIGHT_RANGE])
						inputs[j + 2* square] = 1.0;
					else
						inputs[j + 2 * square] = 0.0;
						
				}
				
				double[][] outputs = network.fullEvaluate(inputs); //network evaluates the game state
				double[] outs = outputs[outputs.length - 1]; //take just the output layer
				
				int dir = getGreatestIndex(outs); //find the direction the network picked
				b.movePlayer(dir); //move in the direction the network picked
				
				
				b.step(); //advance the rest of the board (monsters move)
				
				double[] trainer = Arrays.copyOf(outs, outs.length); //the values to train the network with
				//If the player dies, correct it to not move that way				
				if(!b.getPlayerStatus()) {
					//System.out.println("Died on turn " + b.getTurn() + " with " + b.getGoldCount() + " gold.");
					trainer[dir] = 0.0;
				}
				//reward network for finding gold
				else if(b.getGoldCount() > gold) {
					if(b.foundAllGold())
						System.out.println("COMPLETED GAME: Computer found all " + b.getGoldCount() + " gold in round " + i + " in " + b.getTurn() + " turns.");
					gold = b.getGoldCount();
					trainer[dir] = 1.0;
				}
				//poke the network for visiting the same tile
				else if(b.getBreadCrumbs()){
					trainer[dir] = outs[dir] * PASSIVE_PUNISHMENT;
				}
				//reward it for visiting a new tile?
				else {
					trainer[dir] = outs[dir] / PASSIVE_PUNISHMENT; //this may cause training values > 1.0 early on
				}	
				network.backProp(outputs, trainer); //train the network with the backpropagation algorithm
				
			}
			
			if(b.getTurn() > record) {
				record = b.getTurn();
				System.out.println("NEW RECORD: Computer lasted " + record + " turns in round " + i + " and found " + gold + " out of " + b.getTotalGold() + " gold.");
				System.out.println(b);
			}
			
			if(gold > goldRecord) {
				goldRecord = gold;
				System.out.println("NEW GOLD RECORD: Computer found " + gold + " out of " + b.getTotalGold() + " gold in " + b.getTurn() + " turns in round " + i + ".");
			}
		}
	}

	public static double getFearLevel() {
		return fearLevel;
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