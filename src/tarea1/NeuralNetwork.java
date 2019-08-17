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

        ArrayList<double[][]> cache_list = new ArrayList<>();

        // For each layer in the network
        for(Layer layer: layers){

            // We get the layer cache (outputs for each data row)
            double[][] layer_cache = layer.layer_cache(data);
            cache_list.add(layer_cache);
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

    public void train(double[][] data, double[][] desired_output, int iterations){
        // For each iteration
        for(int it=0; it<iterations; it++){
            // Forward propagation
            ArrayList<double[][]> cache_list = forward_prop(data);
            // Backward propagation
            ArrayList<double[][]> gradient_list = backward_prop(data, desired_output, cache_list);
            // Update weights
            update_weights(gradient_list);
        }
    }

}
