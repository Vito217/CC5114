package tarea1;

import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.xml.crypto.Data;

public class Test {

    public static DataTuple generate_random_binary_dataset(int rows, double m, double b){
        Random r = new Random();
        double[][] dataset = new double[rows][2];
        double[][] output = new double[rows][1];
        for(int i=0; i<rows; i++){
            for(int j=0; j<2; j++){
                dataset[i][j] = r.nextDouble();
            }
            if(m*dataset[i][0] + b >= dataset[i][1]){
                output[i][0] = 0;
            }
            else{
                output[i][0] = 1;
            }
        }
        return new DataTuple(dataset, output);
    }

    public static void main(String[] args){

        DataTuple train_data = generate_random_binary_dataset(1000, 1,0);

        DataTuple eval_data = generate_random_binary_dataset(500, 1,0);

        //Perceptron p = new Perceptron(2, 0.1, "binary", "cross");

        //p.train(10, train_data.getInputs(), train_data.getOutput());
        //DataTuple dt = p.evaluate(eval_data.getInputs(), eval_data.getOutput());

        //SwingUtilities.invokeLater(() -> {
        //    MyPlot example = new MyPlot("Title", dt, 1, 0);
        //    example.setSize(800, 400);
        //    example.setLocationRelativeTo(null);
        //    example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //    example.setVisible(true);
        //});

        //System.out.println("El test paso correctamente");

        //ArrayList<Number> a = new ArrayList<Number>(Collections.nCopies(60, null));
        //System.out.println(a.get(5));

        NeuralNetwork net = new NeuralNetwork(
                new Layer[]{
                        new Layer(1,
                                  2,
                                  0.1,
                                  "sigmoid",
                                  "cross")
                }
        );

        net.train(train_data.getInputs(), train_data.getOutput(), 100);
        net.eval(eval_data.getInputs(), eval_data.getOutput());
    }
}
