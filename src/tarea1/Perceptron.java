package tarea1;

import java.util.Random;

public class Perceptron {

    protected double[] weights;
    protected double bias;
    protected double learning_rate = 0.1;

    public Perceptron(){}

    public Perceptron(int dimensions) {
        Random r = new Random();
        weights = new double[dimensions];
        for(int i=0; i<dimensions; i++){
            weights[i] = -2.0 + 4.0 * r.nextDouble();
        }
        bias = -2.0 + 4.0 * r.nextDouble();
    }

    public Perceptron(double[] weights, double bias) {
        this.weights = weights;
        this.bias = bias;
    }

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

    public void train(int iterations, double[][] train_data){
        
        System.out.println("Initial weights");
        for(double w: weights){
            System.out.println(w);
        }
        
        // Every iteration
        for(int it=0; it<iterations; it++) {
            // For each row in the dataset
            for (int i = 0; i < train_data.length; i++) {
                double[] input = new double[train_data[i].length-1];
                for(int j = 0; j<input.length; j++){
                    input[j] = train_data[i][j];
                }
                double real_output = evaluate(input);
                double desired_output = train_data[i][train_data[i].length-1];
                double diff = desired_output - real_output;
                for(int k = 0; k<weights.length; k++){
                    weights[k] = weights[k] + learning_rate*input[k]*diff;
                }
                bias = bias + learning_rate*diff;
            }
        }

        System.out.println("Final weights");
        for(double w: weights){
            System.out.println(w);
        }
    }
}
