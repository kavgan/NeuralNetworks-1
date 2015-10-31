//Represents a layer of neurons in an artificial neural network
public class NLayer {

	private Neuron[] neurons;
	private int numDendrites;

	public NLayer() {
		
	}
	
	// constructor creates a layer of 'size' neurons with the appropriate number of dendrites
	public NLayer(int size, int numD, boolean isOut) {

		neurons = new Neuron[size];
		numDendrites = numD;
		for (int i = 0; i < size; i++)
			neurons[i] = new Neuron(numDendrites);

	}

	// evaluates the layer, returning each neuron's values
	public double[] evaluate(double input[]) {

		// check for appropriately sized input
		if (input.length != neurons[0].getNumDendrites())
			return null;

		double output[] = new double[neurons.length];

		// fire each neuron and get activation levels
		for (int i = 0; i < neurons.length; i++) {
			
			//fear-dependent evaluation
			/*if(i % 2 == 0)
				neurons[i].specialEvaluate(input, true);
			else
				neurons[i].specialEvaluate(input, false);*/
			
			//normal evaluation
			neurons[i].evaluate(input);
			
			output[i] = neurons[i].getActivationLevel();
		}

		return output;
	}

	public Neuron getNeuron(int i) {
		return neurons[i];
	}

	public int getSize() {
		return neurons.length;
	}
	
	public int getNumDendrites() {
		return numDendrites;
	}

	public void setNeuron(Neuron n, int i) {
		neurons[i] = n;
	}
	
	public void setWeight(double w, int n, int i) {
		neurons[n].setWeight(w, i);
	}
	
	public void setBias(double b, int n) {
		neurons[n].setBias(b);
	}

	public String toString() {

		String ret = "";
		for (int i = 0; i < neurons.length; i++) {
			ret += neurons[i].toString();
			if (i < neurons.length - 1)
				ret += "\n";
		}
		return "" + ret;

	}

}
