//Represents a single neuron in a neural network, including all of its dendrite weights
public class Neuron {

	private final static double DEFAULT_WEIGHT = 1;
	private final static double DEFAULT_BIAS = 0;
	private double bias;
	private double[] weights; // weights of the inputs (dendrites)
	private double aLevel; // activation level

	// default constructor assumes this is input neuron (no dendrites)
	public Neuron() {
		this(0);
	}

	// constructor takes number of dendrites (number of neurons in previous layer)
	public Neuron(int numDendrites) {

		bias = DEFAULT_BIAS;
		weights = new double[numDendrites];
		for (int i = 0; i < numDendrites; i++) {
			weights[i] = DEFAULT_WEIGHT * 2 / numDendrites; // normalize weight based on number of dendrites
			weights[i] = weights[i] * Math.random(); // randomize
		}
		aLevel = 0;
		
	}

	// evaluates the neuron's activation level given the layer before it, returns the effect the previous layer had
	public void evaluate(double[] input) {

		// check for appropriately sized input
		if (input.length != weights.length)
			return;

		// calculate net input
		double sum = 0.0;
		for (int i = 0; i < input.length; i++) {
			sum += input[i] * weights[i];
		}
		sum += bias; //add the bias to the net input (bias * 1)

		// calculate activation level using log-sigmoid function (squash it)
		aLevel = 1 / (1 + Math.pow(Math.E, (-1 * sum)));
		
		//calculate activation level using arctan function
		//aLevel = 2 * Math.atan(bias + sum) / Math.PI;

	}
	
	//evaluates the neuron differently based on the mode and fear level (true for fearful neuron)
	public void specialEvaluate(double[] input, boolean mode) {
		evaluate(input);
		if(mode)
			aLevel -= Control.getFearLevel();
		else
			aLevel += Control.getFearLevel();
	}

	public void deactivate() {
		aLevel = 0.0;
	}

	public double getActivationLevel() {
		return aLevel;
	}

	public double getBias() {
		return bias;
	}

	public int getNumDendrites() {
		return weights.length;
	}
	
	public double[] getWeights() {
		return weights;
	}

	public double getWeight(int i) {
		return weights[i];
	}

	public void setBias(double b) {
		bias = b;
	}

	public void setWeights(double[] w) {
		weights = w;
	}

	public void setWeight(double w, int i) {
		weights[i] = w;
	}

	public String toString() {

		return "[" + bias + ", " + weightsToString() + "]";

	}

	public String weightsToString() {

		String ret = "";
		for (int i = 0; i < weights.length; i++) {
			ret += weights[i];
			if (i < weights.length - 1)
				ret += ", ";
		}
		return "" + ret;
	}

}
