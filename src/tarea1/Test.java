package tarea1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

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

    public static DataTuple read_iris_dataset(String file){

        double[][] data = new double[150][4];
        double[][] target = new double[150][3];

        try {

            Scanner f = new Scanner(new File(file));
            int row = 0;

            // Getting data
            while(f.hasNextLine()){
                String l = f.nextLine();
                String[] d = l.split(",");
                for(int col=0; col<d.length; col++){
                    try{
                        data[row][col] = Double.parseDouble(d[col]);
                    }
                    catch (NumberFormatException e){
                        switch (d[col]) {
                            case "Iris-setosa":
                                target[row] = new double[]{1.0, 0.0, 0.0};
                                break;
                            case "Iris-versicolor":
                                target[row] = new double[]{0.0, 1.0, 0.0};
                                break;
                            case "Iris-virginica":
                                target[row] = new double[]{0.0, 0.0, 1.0};
                                break;
                        }
                    }
                }
                row++;
            }

            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new DataTuple(data, target);
    }

    public static double[][] normalize_iris_data(double[][] data){

        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                data[i][j] = (((data[i][j]-0.0)*(1.0-0.0))/(9.9-0.0)) + 0.0;
            }
        }

        return data;
    }

    public static void main(String[] args){

        /**
        DataTuple train_data = generate_random_binary_dataset(1000, 1,0);

        DataTuple eval_data = generate_random_binary_dataset(500, 1,0);

        NeuralNetwork net = new NeuralNetwork(
                new Layer[]{
                        new Layer(1,
                                  2,
                                  0.1,
                                  "sigmoid",
                                  "cross")
                }
        );

        net.train(train_data.getInputs(), train_data.getOutput(), 1000);
        DataTuple dt = net.eval(eval_data.getInputs(), eval_data.getOutput());

        SwingUtilities.invokeLater(() -> {
            MyPlot example = new MyPlot("Title", dt, 1, 0);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
        **/

        DataTuple iris_data_tuple =
                read_iris_dataset("C:\\Users\\VictorStefano\\IdeaProjects\\CC5114\\src\\tarea1\\iris.data");

        double[][] data = normalize_iris_data(iris_data_tuple.getInputs());
        double[][] target = iris_data_tuple.getOutput();

        double[][] train_data = new double[90][4];
        double[][] train_target = new double[90][3];

        double[][] eval_data = new double[60][4];
        double[][] eval_target = new double[60][3];

        for(int i=0; i<90; i++){
            train_data[i] = data[i];
            train_target[i] = target[i];
        }

        for(int i=0; i<60; i++){
            eval_data[i] = data[i+90];
            eval_target[i] = target[i+90];
        }

        NeuralNetwork n = new NeuralNetwork(
                new Layer[]{
                        new Layer(
                                3,
                                4,
                                0.1,
                                "sigmoid",
                                "cross"
                        )
                }
        );

        n.train(train_data, train_target, 10000);
        DataTuple dt = n.eval(eval_data, eval_target);
        double[][] eval_output = dt.getOutput();

        // Confusion matrix
        double[][] conf_matrix = new double[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<20 && i==0; j++){
                if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][0]){
                    conf_matrix[i][0] += 1.0/20.0;
                }
                else if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][1]){
                    conf_matrix[i][1] += 1.0/20.0;
                }
                else{
                    conf_matrix[i][2] += 1.0/20.0;
                }
            }
            for(int j=20; j<40 && i==1; j++){
                if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][0]){
                    conf_matrix[i][0] += 1.0/20.0;
                }
                else if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][1]){
                    conf_matrix[i][1] += 1.0/20.0;
                }
                else{
                    conf_matrix[i][2] += 1.0/20.0;
                }
            }
            for(int j=40; j<60 && i==2; j++){
                if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][0]){
                    conf_matrix[i][0] += 1.0/20.0;
                }
                else if(Math.max(eval_output[j][0], Math.max(eval_output[j][1], eval_output[j][2]))==eval_output[j][1]){
                    conf_matrix[i][1] += 1.0/20.0;
                }
                else{
                    conf_matrix[i][2] += 1.0/20.0;
                }
            }
        }

        for(double[] row: conf_matrix){
            System.out.println(Arrays.toString(row));
        }
    }
}
