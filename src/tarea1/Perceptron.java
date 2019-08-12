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

    public double activation_value(double[] inputs){
        try{
            if((inputs.length-1) != weights.length) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        double res = bias;
        for(int i=0; i<inputs.length-1; i++){
            res += weights[i]*inputs[i];
        }
        return res;
    }

    public double activation_function(String fun, double val){
        try{
            switch(fun){
                case "binary":
                    return val <= 0 ? 0 : 1;
                case "sigmoid":
                    return 1 / (1+Math.exp(-val));
            }
            throw new Exception();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public void train(String fun, int iterations, double[][] train_data){
        // Every iteration
        for(int it=0; it<iterations; it++) {
            // For each row in the dataset
            for (int i = 0; i < train_data.length; i++) {
                double real_output = activation_function(fun, activation_value(train_data[i]));
                double desired_output = train_data[i][train_data[i].length-1];
                double diff = desired_output - real_output;
                for(int k = 0; k<train_data[i].length-1; k++){
                    weights[k] = weights[k] + learning_rate*train_data[i][k]*diff;
                }
                bias = bias + learning_rate*diff;
            }
        }
    }

    public double[][] evaluate(String fun, double[][] eval_data){
        double correct_answers = 0;
        double total_answers = 0;

        double[][] result_data = new double[eval_data.length][eval_data[0].length];

        for(int i=0; i<eval_data.length; i++){
            double res = activation_function(fun, activation_value(eval_data[i]));
            System.out.println("Desired = "+Double.toString(eval_data[i][eval_data[0].length-1]));
            System.out.println("Obtained = "+Double.toString(res));
            if(Math.round(res) == Math.round(eval_data[i][eval_data[0].length-1])){
                correct_answers++;
            }
            total_answers++;
            double[] aux = eval_data[i];
            aux[aux.length-1] = res;
            result_data[i] = aux;
        }
        double acc = correct_answers / total_answers;
        System.out.println("Accuracy = "+Double.toString(acc));
        return result_data;
    }
}
