
public class NNetwork {

	private NLayer inputLayer;
	private NLayer[] hiddenLayers;
	private NLayer outputLayer;
	
	/*
	 * Constructor takes the number of neurons in the input layer,
	 * the numbers of neurons in the hidden layers, and the number
	 * of neurons in the output layer.
	 */
	public NNetwork(int numInputs, int[] sizes, int numOutputs) {
		
		inputLayer = new NLayer(numInputs);
		
		hiddenLayers = new NLayer[sizes.length];
		for(int i = 0; i < sizes.length; i++) {
			if(i == 0)
				hiddenLayers[0] = new NLayer(sizes[0], numInputs);
			else
				hiddenLayers[i] = new NLayer(sizes[i], sizes[i - 1]);
		}
		
		if(sizes.length > 0)
			outputLayer = new NLayer(numOutputs, sizes[sizes.length - 1]);
		else
			outputLayer = new NLayer(numOutputs, numInputs);
		
	}
	
}
