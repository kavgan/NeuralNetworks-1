//Represents an artificial neural network composed of 2+ NLayers
public class NNetwork {

	final static private double LEARNING_RATE = 0.5;
	final static private double BIAS_LEARNING_RATE = 0.2;
	
	private int inputSize;
	public NLayer[] hiddenLayers;
	private NLayer outputLayer;
	
	//manual constructor
	public NNetwork(int i, NLayer[] h, NLayer o) {
		inputSize = i;
		hiddenLayers = h;
		outputLayer = o;
	}

	//Constructor takes the number of neurons in the input layer, the numbers of neurons in the hidden layers, and the number of neurons in the output layer.
	public NNetwork(int numInputs, int[] sizes, int numOutputs) {

		inputSize = numInputs;

		hiddenLayers = new NLayer[sizes.length];
		for (int i = 0; i < sizes.length; i++) {
			if (i == 0)
				hiddenLayers[0] = new NLayer(sizes[0], numInputs, false);
			else
				hiddenLayers[i] = new NLayer(sizes[i], sizes[i - 1], false);
		}

		if (sizes.length > 0)
			outputLayer = new NLayer(numOutputs, sizes[sizes.length - 1], true);
		else
			outputLayer = new NLayer(numOutputs, numInputs, true);

	}
	
	//Back propagation algorithm for neural network learning
	public void backProp(double[][] outputs, double[] expected) {
		
		//check valid output size
		int outSize = outputLayer.getSize();
		if(outputs[outputs.length - 1].length != outSize || outSize != expected.length) {
			System.err.println("ERROR: Invalid output size");
			return;
		}
		
		//remember the old weights
		double[][] oldOutWeights = new double[outSize][outputLayer.getNumDendrites()];
		double[][][] oldHiddenWeights = new double[hiddenLayers.length][][];

		// 1.) Correct output layer dendrite weights
		
		double[] errTot_out_outLayer = new double[outSize]; //the derivatives of total error over neuron output for the output layer
		double[] out_netIn_outLayer = new double[outSize]; //the derivatives of neuron output over net input for the output layer
		
		//loop through neurons in the output layer
		for(int n = 0; n < outSize; n++) {
			
			double o = outputs[outputs.length - 1][n]; //output for this neuron
			errTot_out_outLayer[n] = o - expected[n]; //total error change with respect to output
			out_netIn_outLayer[n] = o * (1 - o); //output change with respect to net input
			
			//loop through dendrites of this neuron
			for(int d = 0; d < outputLayer.getNumDendrites(); d++) {
				double netIn_weight_outLayer = outputs[outputs.length - 2][d]; //net input change with respect to weight of dendrite
				double errTot_weight_outLayer = errTot_out_outLayer[n] * out_netIn_outLayer[n] * netIn_weight_outLayer; //total error change with respect to weight of dendrite
				double newWeight = outputLayer.getNeuron(n).getWeight(d); //get the dendrite's weight
				newWeight -= LEARNING_RATE * errTot_weight_outLayer; //subtract from it the correction * learning rate
				oldOutWeights[n][d] = outputLayer.getNeuron(n).getWeight(d);
				outputLayer.setWeight(newWeight, n, d); //store the new weight
			}
			
			//fix the bias
			double newBias = outputLayer.getNeuron(n).getBias(); //get the neuron's bias
			newBias -= BIAS_LEARNING_RATE * errTot_out_outLayer[n] * out_netIn_outLayer[n]; //subtract from it the correction * bias learning rate
			outputLayer.setBias(newBias, n);
			
		}
		
		// 2.) Correct hidden layer dendrite weights
		
		double[][] errTot_out = new double[hiddenLayers.length][]; //the derivatives of total error over neuron output for the hidden layers
		double[][] out_netIn = new double[hiddenLayers.length][]; //the derivatives of neuron output over net input for the hidden layers
		
		//loop through hidden layers
		for(int l = hiddenLayers.length - 1; l >= 0; l--) {
			
			oldHiddenWeights[l] = new double[hiddenLayers[l].getSize()][hiddenLayers[l].getNumDendrites()]; //old weights initialization
			
			errTot_out[l] = new double[hiddenLayers[l].getSize()]; //set to appropriate size for this layer
			out_netIn[l] = new double[hiddenLayers[l].getSize()]; //set to appropriate size for this layer
			
			//loop through neurons in this layer
			for(int n = 0; n < hiddenLayers[l].getSize(); n++) {
				
				double o = outputs[1 + l][n]; //output for this neuron
				errTot_out[l][n] = 0; //start at 0 so can add to it
				out_netIn[l][n] = o * (1 - o);			
				
				//handle differently if this is the last hidden layer
				if(l == hiddenLayers.length - 1)
					for(int a = 0; a < outputLayer.getSize(); a++) { //loop through axons (dendrites of next layer)
						double e_netOut = errTot_out_outLayer[a] * out_netIn_outLayer[a];
						errTot_out[l][n] += e_netOut * oldOutWeights[a][n];
	
					}
				else
					for(int a = 0; a < hiddenLayers[l + 1].getSize(); a++) { //loop through axons (dendrites of next layer)
						double e_netOut = errTot_out[l + 1][a] * out_netIn[l + 1][a];
						errTot_out[l][n] += e_netOut * oldHiddenWeights[l + 1][n][a];
					}
				
				//loop through dendrites of this neuron
				for(int d = 0; d < hiddenLayers[l].getNumDendrites(); d++) {
					double netIn_weight = outputs[l][d]; //net input change with respect to weight of dendrite
					double errTot_weight = errTot_out[l][n] * out_netIn[l][n] * netIn_weight; //total error change with respect to weight of dendrite
					double newWeight = hiddenLayers[l].getNeuron(n).getWeight(d); //get the dendrite's weight
					newWeight -= LEARNING_RATE * errTot_weight; //subtract from it the correction * learning rate
					oldHiddenWeights[l][n][d] = hiddenLayers[l].getNeuron(n).getWeight(d);
					hiddenLayers[l].setWeight(newWeight, n, d); //store the new weight
				}
				
				//fix the bias
				double newBias = hiddenLayers[l].getNeuron(n).getBias(); //get the neuron's bias
				newBias -= BIAS_LEARNING_RATE * errTot_out[l][n] * out_netIn[l][n]; //subtract from it the correction * bias learning rate
				hiddenLayers[l].setBias(newBias, n);
				
			}
				
		}

	}

	// fires all the input neurons and returns the outputs
	public double[] evaluate(double[] input) {

		// check for appropriately sized input
		if (input.length != inputSize)
			return null;

		// fire hidden layers
		double[] activationFront = new double[0];
		if (hiddenLayers.length > 0)
			activationFront = hiddenLayers[0].evaluate(input);
		for (int i = 1; i < hiddenLayers.length; i++)
			activationFront = hiddenLayers[i].evaluate(activationFront);

		// fire output layer
		activationFront = outputLayer.evaluate(activationFront);

		return activationFront;

	}
	
	// fires all the input neurons and returns all outputs of all layers
	public double[][] fullEvaluate(double[] input) {
		
		// check for appropriately sized input
		if (input.length != inputSize)
			return null;
		
		double[][] allOutputs = new double[2 + hiddenLayers.length][];
		
		//add the input to the returned outputs
		allOutputs[0] = input;
		
		//fire hidden layers
		for(int i = 0; i < hiddenLayers.length; i++) {
			allOutputs[i + 1] = hiddenLayers[i].evaluate(input);
			input = allOutputs[i + 1];
		}
		//fire output layer
		allOutputs[hiddenLayers.length + 1] = outputLayer.evaluate(input);
		
		return allOutputs;
		
	}

	public int getNumLayers() {
		return 2 + hiddenLayers.length;
	}

	public int getInputSize() {
		return inputSize;
	}
	
	public NLayer[] getHiddenLayers() {
		return hiddenLayers;
	}

	public NLayer getOutputLayer() {
		return outputLayer;
	}

	public NLayer getLayer(int i) {
		if (i == 0)
			return null;
		else if (i <= hiddenLayers.length)
			return hiddenLayers[i - 1];
		else if (i == hiddenLayers.length + 1)
			return outputLayer;
		else
			return null;
	}

	public String toString() {

		String ret = "";
		for (int i = 0; i < hiddenLayers.length; i++) {
			ret += hiddenLayers[i] + "\n";
		}
		ret += outputLayer.toString();
		return "" + ret;

	}

}
