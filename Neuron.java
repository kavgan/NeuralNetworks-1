
public class Neuron {
	
	private final static double DEFAULT_THRESHOLD = 1;
	private final static double DEFAULT_WEIGHT = 1;
	private double threshold;
	private double[] weights; //weights of the inputs (dendrites)
	private boolean active;
	
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
		active = false;
		
	}
	
	//constructor takes weights for all the dendrites and the neuron's activation threshold
	public Neuron(double[] dendriteWeights, double activationThreshold) {
		
		threshold = activationThreshold;
		weights = dendriteWeights;
		active = false;
		
	}
	
	public void activate() {
		active = true;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public boolean getIsActive() {
		return active;
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
}
