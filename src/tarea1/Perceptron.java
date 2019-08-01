package tarea1;

public class Perceptron {

    protected double[] weights;
    protected double bias;

    public Perceptron(){}

    public double evaluate(double[] inputs){
        try{
            if(inputs.length != weights.length) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        double res = bias;
        for(int i=0; i<weights.length; i++){
            res += weights[i]*inputs[i];
        }
        if(res > 0){
            return 1;
        }
        else{
            return 0;
        }
    }
}
