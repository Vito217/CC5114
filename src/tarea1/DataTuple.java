package tarea1;

public class DataTuple {

    private double[][] inputs;
    private double[][] output;

    public DataTuple(double[][] inputs, double[][] output){
        this.inputs = inputs;
        this.output = output;
    }

    public double[][] getInputs(){
        return inputs;
    }

    public double[][] getOutput(){
        return output;
    }
}
