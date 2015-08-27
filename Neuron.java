
public class Neuron {
	
	private final static double DEFAULT_THRESHOLD = 1;
	private final static double DEFAULT_WEIGHT = 1;
	private double threshold;
	private double[] weights; //weights of the inputs (dendrites)
	private double aLevel; //activation level
	
	//default constructor assumes this is input neuron (no dendrites)
	public Neuron() {
		this(0);
	}
	
	//constructor takes number of dendrites (number of neurons in previous layer)
	public Neuron(int numDendrites) {
		
		threshold = DEFAULT_THRESHOLD;
		weights = new double[numDendrites];
		for(int i = 0; i < numDendrites; i++) {
			weights[i] = DEFAULT_WEIGHT * 2 / numDendrites; //normalize weight based on number of dendrites
			weights[i] = Math.round(weights[i] * Math.random() * 10.0) / 10.0; //round to nearest tenth
		}
		aLevel = 0;
		
	}
	
	//constructor takes weights for all the dendrites and the neuron's activation threshold
	public Neuron(double[] dendriteWeights, double activationThreshold) {
		
		threshold = activationThreshold;
		weights = dendriteWeights;
		aLevel = 0;
		
	}
	
	//evaluates the neuron's activation level given the layer before it
	public void evaluate(double[] input) {
		
		//check for appropriately sized input
		if(input.length != weights.length)
			return;
		
		//calculate total activation
		double sum = 0.0;
		for(int i = 0; i < input.length; i++) {
			sum += input[i] * weights[i];
		}
		
		//calculate activation level using log-sigmoid function
		aLevel = 1 / (1 + Math.pow(Math.E, (-sum)));
		
	}
	
	public void deactivate() {
		aLevel = 0.0;
	}
	
	public double getActivationLevel() {
		return aLevel;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public double getWeight(int i) {
		return weights[i];
	}
	
	public void setThreshold(double t) {
		threshold = t;
	}
	
	public void setWeights(double[] w) {
		weights = w;
	}
	
	public void setWeight(double w, int i) {
		weights[i] = w;
	}
	
	public String toString() {
		
		return "[" + threshold + "]";
		
	}
	
	public String weightsToString() {
		
		String ret = "[";
		for(int i = 0; i < weights.length; i++) {
			ret.concat(weights[i] + "");
			if(i < weights.length - 1)
				ret.concat(", ");
		}
		return ret;
	}
	
}
