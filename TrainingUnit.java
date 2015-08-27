//TODO: create TrainingSet class that reads from file into an array of TrainingUnits

public class TrainingUnit {
	
	private double inputs[];
	private double outputs[];
	
	public TrainingUnit(double[] i, double[] o) {
		inputs = i;
		outputs = o;
	}
	
	public double[] getInputs() {
		return inputs;
	}
	
	public double[] getOutputs() {
		return outputs;
	}
	
	public double getInput(int i) {
		return inputs[i];
	}
	
	public double getOutput(int i) {
		return outputs[i];
	}
	
	public double getNumInputs() {
		return inputs.length;
	}
	
	public double getNumOutputs() {
		return outputs.length;
	}
	
	public void setInputs(double[] i) {
		inputs = i;
	}
	
	public void setOutputs(double[] o) {
		outputs = o;
	}
	
	public void setInput(double d, int i) {
		if(i >= 0 && i < getNumInputs())
			inputs[i] = d;
	}
	
	public void setOutput(double d, int i) {
		if(i >= 0 && i < getNumOutputs())
			outputs[i] = d;
	}

}
