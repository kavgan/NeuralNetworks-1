import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.IntPredicate;

public class Control {

	private static Scanner scan = new Scanner(System.in);
	private static TabbedPrint out = new TabbedPrint();
	private static NNetwork network; //the neural network being used (assumes only using one at a time)
	
	public static void main(String[] args) {
		
		String cmd; //the user-entered command
		
		//loop until user quits
		while(true) {
			
			System.out.print("$ ");
			cmd = scan.nextLine();
			
			//catch commands
			if(cmd.equalsIgnoreCase("new"))
				createNetwork();
			else if (cmd.equalsIgnoreCase("print"))
				printNetwork();
			else if (cmd.equalsIgnoreCase("weights"))
				printWeights();
			else if(cmd.substring(0,4).equalsIgnoreCase("exit"))
				break;
			else
				System.out.println("Command not recognized.");
			
		}
		
		scan.close();
		
	}
	
	public static void createNetwork() {
		
		//ask the user for the new neural net parameters
		out.println("How many inputs?");
		int numInputs = readNumber(0, (x) -> x > 0);
		out.println("How many hidden layers?");
		int[] hiddenLayerSizes = new int[readNumber(0, (x) -> x > 0)];
		for(int i = 0; i < hiddenLayerSizes.length; i++) {
			out.println("What is the size of hidden layer number " + (i + 1) + "?");
			hiddenLayerSizes[i] = readNumber(0, (x) -> x > 0); 
		}
		out.println("How many outputs?");
		int numOutputs = readNumber(0, (x) -> x > 0);
		
		//create the neural network
		network = new NNetwork(numInputs, hiddenLayerSizes, numOutputs);
		
	}
	
	public static void printNetwork() {
		
		if(network != null) 
			out.println(network);
		else
			out.println("Cannot print non-instantiated network.");
	}
	
	public static void printWeights() {
		
		if(network != null) {
			//ask the user to identify which neuron's weights
			out.println("Which layer? (i for input, o for output, 1-n for hidden layers)");
			String layer = null, neur = null;
			int layNum = 0, neurNum = 0;
			while(layer == null){
				out.print("");
				layer = scan.nextLine().substring(0, 1);
				layNum = Integer.valueOf(layer);
				if(!(layer == "i" || layer == "o" || (layNum >= 0 && layNum <= network.getNumLayers()))){
					out.println("Invalid layer number.");
					layer = null;
				}
			}
			NLayer lay = network.getLayer(layNum);
			while(neur == null) {
				out.print("");
				neur = scan.nextLine();
				neurNum = Integer.valueOf(neur);
				if(!(neurNum >= 0 && neurNum <= lay.getSize())){
					out.println("Invalid neuron number.");
					neur = null;
				}
			}
			out.println(lay.getNeuron(neurNum).weightsToString());
		}
		else
			out.println("Cannot get weights of non-instantiated network.");
		
		
	}
	
	//gets a number from the user that fulfills an IntPredicate (lambda expression)
	public static int readNumber(int defaultNum, IntPredicate p) {
		
		while(!p.test(defaultNum)) {
			try{
				out.print("");
				defaultNum = scan.nextInt();
			} catch(Exception e) {
				out.println("Please enter a number");
			}
			if(!p.test(defaultNum))
				out.println("Invalid number.");
		}
		
		return defaultNum;
	}
	
	//formats with a two space indent
	private static class TabbedPrint extends PrintStream {

		public TabbedPrint() {
			super(System.out);
		}
		
		public void print(String s) {
			super.print("  ".concat(s));
		}
		
	}
	
}
