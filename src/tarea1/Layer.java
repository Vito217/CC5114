package tarea1;

public class Layer {

    Perceptron[] neurons;

    public Layer(int input_size, int n_neurons, double learning_rate, String act_fun, String loss_fun){
        neurons = new Perceptron[n_neurons];
        for(int i=0; i<n_neurons; i++){
            neurons[i] = new Perceptron(input_size, learning_rate, act_fun, loss_fun);
        }
    }

    double[][] layer_cache(double[][] data){

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

        double[][] layer_cache = new double[neurons.length][data.length];

        // For each neuron in the layer
        for(int i=0; i<neurons.length; i++){

            // For each row in the dataset
            for(int j=0; j<data.length; j++){

                // We get the output from the neuron
                double cache =  neurons[i].activation_function(neurons[i].activation_value(data[j]));
                layer_cache[i][j] = cache;
            }
        }
        return layer_cache;
    }

    double[][] layer_gradient(double[][] data, double[][] layer_cache, double[][] desired_output,
                                     Layer next_layer, double[][] last_layer_gradient){

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

        double[][] layer_gradient= new double[neurons.length][data.length];

        // If this is the last layer, we obtain gradient normally
        // i.e., err_fun_derivative * act_fun_derivative
        if(next_layer==null){

            // For each neuron in the layer
            for(int i=0; i<neurons.length; i++) {

                // For each input that enters the neuron
                for(int j=0; j<data.length;j++){

                    // We get the delta
                    layer_gradient[i][j] = neurons[i].loss_function(layer_cache[i][j], desired_output[j][i]);
                }
            }
        }
        // If this is an inner layer (or the first layer)
        // we obtain the gradient as:
        //    dw_i = sum(delta_k * weight_k) * act_fun_derivative
        // where:
        //    dw_i    = gradient for weight i
        //    delta_k = gradient of next layer's weight k
        else{

            // For each neuron
            for(int i=0; i<neurons.length; i++) {

                // For each input that enters the neuron
                for(int j=0; j<data.length;j++){

                    // First, we get the sum of delta * weights for the next layer
                    double sum = 0;
                    for(int k=0; k<next_layer.neurons.length; k++){

                        // last_layer_gradient: from next layer, neuron k, input j
                        // next_layer.neurons[k].weights[i]: we use the weight i that
                        //                                   recieves the input from neuron i.
                        sum += last_layer_gradient[k][j] * next_layer.neurons[k].weights[i];
                    }

                    // Then, we get the gradient
                    layer_gradient[i][j] = sum * neurons[i].derivative_activation_function(layer_cache[i][j]);

                }
            }
        }

        return layer_gradient;
    }

    void update_weights(double[][] gradient){
        for(int i=0; i<gradient.length; i++){
            neurons[i].update_weights(gradient[i]);
        }
    }

    double[] evaluate(double[] data){
        double[] output = new double[neurons.length];
        for(int i=0; i<neurons.length; i++){
            output[i] = neurons[i].activation_function(neurons[i].activation_value(data));
        }
        return output;
    }
}
