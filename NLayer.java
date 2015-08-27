
public class NLayer {

	private Neuron[] neurons;
	
	//constructor creates a layer of 'size' neurons with 0 dendrites (input layer)
	public NLayer(int size) {
		this(size, 0);
	}
	
	//constructor creates a layer of 'size' neurons with the appropriate number of dendrites
	public NLayer(int size, int numDendrites) {
		
		neurons = new Neuron[size];
		for(int i = 0; i < numDendrites; i++) {
			neurons[i] = new Neuron(numDendrites);
		}
		
	}
	
	//evaluates the layer, returning each neuron's values
	public double[] evaluate(double input[]) {
		
		//check for appropriately sized input
		if(input.length != neurons.length)
			return null;
		
		double output[] = new double[neurons.length];
		
		//fire each neuron and get activation levels
		for(int i = 0; i < neurons.length; i++) {
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
	
	public void setNeuron(Neuron n, int i) {
		neurons[i] = n;
	}
	
	public String toString() {
		
		String ret = "";
		for(int i = 0; i < neurons.length; i++) {
			ret.concat(neurons[i].toString());
			if(i < neurons.length - 1)
				ret.concat(", ");
		}
		return ret;
		
	}
	
}
