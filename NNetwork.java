
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
	
	//fires all the input neurons and returns the outputs
	public double[] evaluate(double[] input) {
		
		//check for appropriately sized input
		if(input.length != inputLayer.getSize())
			return null;
		
		//fire hidden layers
		double[] activationFront = new double[0];
		if(hiddenLayers.length > 0)
			activationFront = hiddenLayers[0].evaluate(input);
		for(int i = 1; i < hiddenLayers.length; i++) 
			activationFront = hiddenLayers[i].evaluate(activationFront);
		
		//fire output layer
		activationFront = outputLayer.evaluate(activationFront);
		
		return activationFront;
		
	}
	
	public int getNumLayers() {
		return 2 + hiddenLayers.length;
	}
	
	public NLayer getInputLayer() {
		return inputLayer;
	}
	
	public NLayer getOutputLayer() {
		return outputLayer;
	}
	
	public NLayer getLayer(int i) {
		if(i == 0)
			return inputLayer;
		else if(i <= hiddenLayers.length)
			return hiddenLayers[i - 1];
		else if(i == hiddenLayers.length + 1)
			return outputLayer;
		else
			return null;
	}
	
	public String toString() {
		
		String ret = "";
		ret.concat(inputLayer.toString() + "\n");
		for(int i = 0; i < hiddenLayers.length; i++) {
			ret.concat(hiddenLayers[i] + "\n");
		}
		ret.concat(outputLayer.toString());
		return ret;
		
	}
	
}
