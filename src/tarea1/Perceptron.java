package tarea1;

import java.util.Random;

public class Perceptron {

    protected double[] weights;
    protected double bias;
    protected double learning_rate;
    protected String activation_function;
    protected String loss_function;

    public Perceptron(){}

    public Perceptron(int input_size, double learning_rate, String act_fun, String loss_fun) {

        // Create Random weights and Bias
        Random r = new Random();
        weights = new double[input_size];
        for(int i=0; i<input_size; i++){
            weights[i] = -2.0 + 4.0 * r.nextDouble();
        }

        // Create random bias
        bias = -2.0 + 4.0 * r.nextDouble();

        this.learning_rate = learning_rate;
        this.activation_function = act_fun;
        this.loss_function = loss_fun;

    }

    public double activation_value(double[] inputs){
        try{
            if(inputs.length != weights.length) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        double res = bias;
        for(int i=0; i<inputs.length; i++){
            res += weights[i]*inputs[i];
        }
        return res;
    }

    public double activation_function(double val){
        try{
            switch(this.activation_function){
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

    public double derivative_activation_function(double val){
        try{
            switch(this.activation_function){
                case "binary":
                    return 0;
                case "sigmoid":
                    return val*(1-val);
            }
            throw new Exception();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public double loss_function(double ro, double dou){
        try{
            switch(this.loss_function){
                case "mse":
                    return (dou-ro)*ro*(1-ro);
                case "cross":
                    return (dou-ro);
            }
            throw new Exception();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public void train(int iterations, double[][] train_data, double[] train_target){
        // Every iteration
        for(int it=0; it<iterations; it++) {
            // For each row in the dataset
            for (int i = 0; i < train_data.length; i++) {
                // Compute output
                double real_output = activation_function(activation_value(train_data[i]));
                // Get desired output
                double desired_output = train_target[i];
                // Update weights
                for(int k = 0; k<train_data[i].length; k++){
                    weights[k] = weights[k] + learning_rate*train_data[i][k]*
                                                loss_function(real_output, desired_output);
                }
                bias = bias + learning_rate*loss_function(real_output, desired_output);
            }
        }
    }

    public DataTuple evaluate(double[][] eval_data, double[] eval_target){

        // Initialize counters
        double correct_answers = 0;
        double total_answers = 0;

        // Initialize prediction matrix
        double[][] result_data = new double[eval_data.length][eval_data[0].length];
        double[] output = new double[eval_data.length];

        // For each row in data
        for(int i=0; i<eval_data.length; i++){

            // Getting prediction
            double res = activation_function(activation_value(eval_data[i]));

            System.out.println("Desired = "+Double.toString(eval_target[i]));
            System.out.println("Obtained = "+Double.toString(res));

            // Threshold is 0.5 by default
            if(Math.round(res) == Math.round(eval_target[i])){
                correct_answers++;
            }
            total_answers++;

            // Updating predictions matrix
            output[i] = res;
        }
        double acc = correct_answers / total_answers;
        System.out.println("Accuracy = "+Double.toString(acc));
        return new DataTuple(eval_data, output);
    }

    public void update_weights(double[] gradient){
        // Updating weights
        for(int i=0; i<gradient.length-1; i++){
            weights[i] = weights[i] + learning_rate*gradient[i];
        }
        // Updating bias
        bias = bias + learning_rate*gradient[gradient.length-1];
    }

}
