package tarea1;

import java.util.ArrayList;

public class NeuralNetwork {

    private Layer[] layers;

    public NeuralNetwork(Layer[] layers){
        this.layers = layers;
    }

    private ArrayList<double[][]> forward_prop(double[][] data){

        // Cache List for a single layer:
        //
        //    outputs (columns for each data row)
        //         _ _ _ _ _ _ _ _ _ _ _
        // n    i1|
        // e    i2|
        // u     .|
        // r     .|
        // o     .|
        // n     .|
        // s    in|_  _  _  _ _ _ _ _ _
        //         j1 j2 j3 . . . . . jm

        // Initialize cache list
        ArrayList<double[][]> cache_list = new ArrayList<>();
        cache_list.add(layers[0].layer_cache(data));

        // For each layer remaining in the network
        for(int i=1; i<layers.length; i++){

            // We get the layer cache
            double[][] input = cache_list.get(i-1);
            double[][] aux_data = new double[input[0].length][input.length];
            for(int j=0; j<input[0].length; j++){
                for(int k=0; k<input.length; k++){
                    aux_data[j][k] = input[k][j];
                }
            }

            cache_list.add(layers[i].layer_cache(aux_data));
        }

        return cache_list;
    }

    private ArrayList<double[][]> backward_prop(double[][] data, double[][] desired_output,
                                                ArrayList<double[][]> cache_list){

        // Gradient List for a single layer:
        //
        //    outputs (columns for each data row)
        //         _ _ _ _ _ _ _ _ _ _ _
        // n    i1|
        // e    i2|
        // u     .|
        // r     .|
        // o     .|
        // n     .|
        // s    in|_  _  _  _ _ _ _ _ _
        //         j1 j2 j3 . . . . . jd

        ArrayList<double[][]> gradient_list = new ArrayList<>();

        // First, we get gradient for last layer
        double[][] last_layer_gradient =
                layers[layers.length-1].layer_gradient(
                        data,
                        cache_list.get(layers.length-1),
                        desired_output,
                        null,
                        null);

        gradient_list.add(0, last_layer_gradient);

        // For the remaining layers, from finish to start
        for(int i=layers.length-2; i>=0; i--){

            // Get layer gradient using next layer's weights and gradients
            last_layer_gradient =
                    layers[i].layer_gradient(
                            data,
                            cache_list.get(i),
                            desired_output,
                            layers[i+1],
                            last_layer_gradient);

            gradient_list.add(0, last_layer_gradient);
        }

        return mean_backward_prop(data, gradient_list, cache_list);
    }

    private ArrayList<double[][]> mean_backward_prop(double[][] data, ArrayList<double[][]> gradient_list,
                                                     ArrayList<double[][]> cache_list){

        // Final Gradient List for a single layer:
        //
        //             weights + bias
        //         _ _ _ _ _ _ _ _ _ _ _
        // n    i1|
        // e    i2|
        // u     .|
        // r     .|
        // o     .|
        // n     .|
        // s    in|_  _  _  _ _ _ _ _ _
        //         j1 j2 j3 . . . . . jd

        ArrayList<double[][]> mean_gradient_list = new ArrayList<>();
        // For each layer
        for(int l=0; l<layers.length; l++){

            Layer layer = layers[l];
            double[][] layer_gradient = gradient_list.get(l);
            double[][] mean_layer_gradient = new double[layer.neurons.length][layer.neurons[0].weights.length+1];

            // For each neuron in the layer
            for(int i=0; i<layer.neurons.length; i++){
                // For each weight of the neuron
                for(int j=0; j<layer.neurons[i].weights.length; j++){
                    // For each input that enters the layer
                    if(l==0){
                        // First layer recieves data itself
                        for(int k=0; k<data.length; k++){
                            mean_layer_gradient[i][j] += data[k][j]*layer_gradient[i][k]/data.length;
                        }
                    }
                    else{
                        // Inner and output layer recieve cache as input
                        double[][] previous_layer_cache = cache_list.get(l-1);
                        for(int k=0; k<previous_layer_cache[0].length; k++){
                            mean_layer_gradient[i][j] +=
                                    previous_layer_cache[j][k]*layer_gradient[i][k]/data.length;
                        }
                    }
                }
                // Finally, we get the mean gradient for the bias
                if(l==0){
                    for(int k=0; k<data.length; k++){
                        mean_layer_gradient[i][layer.neurons[0].weights.length] +=
                                layer_gradient[i][k]/data.length;
                    }
                }
                else{
                    double[][] previous_layer_cache = cache_list.get(l);
                    for(int k=0; k<previous_layer_cache[0].length; k++){
                        mean_layer_gradient[i][layer.neurons[0].weights.length] +=
                                layer_gradient[i][k]/data.length;
                    }
                }
            }
            mean_gradient_list.add(mean_layer_gradient);
        }
        return mean_gradient_list;
    }

    private void update_weights(ArrayList<double[][]> mean_gradient_list){
        // Updating weights
        for(int i=0; i<layers.length; i++){
            layers[i].update_weights(mean_gradient_list.get(i));
        }
    }

    public Tuple train(double[][] data, double[][] desired_output, int iterations){
        // For each iteration
        double[] loss = new double[iterations];
        double[] success = new double[iterations];
        for(int it=0; it<iterations; it++){
            System.out.println("Step = "+Integer.toString(it+1));
            // Forward propagation
            ArrayList<double[][]> cache_list = forward_prop(data);
            // Backward propagation
            ArrayList<double[][]> gradient_list = backward_prop(data, desired_output, cache_list);
            // Update weights
            update_weights(gradient_list);
            // Get loss
            double it_loss = loss(cache_list.get(cache_list.size()-1),desired_output);
            System.out.println("Loss = "+Double.toString(it_loss));
            loss[it] = it_loss;
            // Get right answers
            double right = successes(cache_list.get(cache_list.size()-1),desired_output);
            System.out.println("Successes = "+Double.toString(right));
            success[it] = right;
        }
        return new Tuple(loss, success);
    }

    public Tuple eval(double[][] eval_data, double[][] eval_target){

        // Initialize counters
        double correct_answers = 0;
        double total_answers = 0;

        // Initialize prediction matrix
        double[][] outputs = new double[eval_target.length][eval_target[0].length];

        // For each row in data
        for(int i=0; i<eval_data.length; i++){

            // Getting prediction
            double[] output = eval_data[i];
            for(Layer layer: layers){
                output = layer.evaluate(output);
            }

            // Threshold is 0.5 by default
            for(int j=0; j<output.length; j++){
                if(Math.round(output[j]) == Math.round(eval_target[i][j])){
                    correct_answers += 1.0/output.length;
                }
                outputs[i][j] = output[j];
            }

            total_answers++;
        }

        double acc = correct_answers / total_answers;
        System.out.println("Accuracy = "+Double.toString(acc));

        return new Tuple(eval_data, outputs);
    }

    public double loss(double[][] real_out, double[][] desired_out){
        double loss = 0;
        // Transposing target
        double[][] aux = new double[desired_out[0].length][desired_out.length];
        for(int j=0; j<desired_out[0].length; j++){
            for(int k=0; k<desired_out.length; k++){
                aux[j][k] = desired_out[k][j];
            }
        }
        //Getting loss per neuron
        Layer last_layer = layers[layers.length-1];
        for(int i=0; i<real_out.length; i++){
            loss += last_layer.neurons[i].loss_function(real_out[i], aux[i])/real_out.length;
        }
        return loss;
    }

    public double successes(double[][] real_out, double[][] desired_out){
        double successes = 0;
        // Transposing target
        double[][] aux = new double[desired_out[0].length][desired_out.length];
        for(int j=0; j<desired_out[0].length; j++){
            for(int k=0; k<desired_out.length; k++){
                aux[j][k] = desired_out[k][j];
            }
        }
        for(int i=0; i<real_out.length; i++){
            for(int j=0; j<real_out[i].length; j++){
                if(Math.round(real_out[i][j]) == Math.round(aux[i][j])){
                    successes++;
                }
            }
        }
        return successes;
    }
}