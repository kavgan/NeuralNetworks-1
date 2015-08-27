
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
	
	public Neuron getNeuron(int i) {
		return neurons[i];
	}
	
	public int getSize() {
		return neurons.length;
	}
	
	public void setNeuron(Neuron n, int i) {
		neurons[i] = n;
	}
	
}
