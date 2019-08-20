package tarea1;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Test {

    private static DecimalFormat numberFormat = new DecimalFormat("#.00");

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

    private static DataTuple read_class_dataset(String file, String separator){

        // Getting classes and number of rows
        int rows = 0;
        ArrayList<String> classes = new ArrayList<>();
        try {
            Scanner f = new Scanner(new File(file));
            while(f.hasNextLine()){
                String l = f.nextLine();
                String[] d = l.split(separator);
                if(!classes.contains(d[d.length-1])){
                    classes.add(d[d.length-1]);
                }
                rows++;
            }
            f.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //Getting data
        double[][] data = new double[rows][];
        double[][] target = new double[rows][];
        int row = 0;
        try {
            Scanner f = new Scanner(new File(file));
            while (f.hasNextLine()) {
                String l = f.nextLine();
                String[] d = l.split(separator);
                //System.out.println(Arrays.toString(d));
                // Saving data_row
                double[] data_row = new double[d.length-1];
                for(int i=0; i<d.length-1; i++){
                    data_row[i] = Double.parseDouble(d[i]);
                }
                data[row] = data_row;
                // Saving target_row
                double[] target_row = new double[classes.size()];
                target_row[classes.indexOf(d[d.length-1])] = 1.0;
                target[row] = target_row;
                // Updating row
                row++;
            }
            f.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return new DataTuple(data, target);
    }

    private static double[][] normalize_data(double[][] data, double nh, double nl){
        double dl = Double.MAX_VALUE;
        double dh = Double.MIN_VALUE;
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                if(data[i][j]<dl){dl=data[i][j];}
                if(data[i][j]>dh){dh=data[i][j];}
            }
        }
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                data[i][j] = (((data[i][j]-dl)*(nh-nl))/(dh-dl)) + nl;
            }
        }
        return data;
    }

    private static double[][] confusion_matrix(double[][] real_output){

        // Initialize matrix
        double[][] matrix = new double[real_output[0].length][real_output[0].length];

        // We get number of rows per class
        int rows_per_group = real_output.length/real_output[0].length;

        // For each row in the matrix
        for(int i=0; i<real_output[i].length; i++){

            double total = 0.0;
            // For each row in real output that belongs to class i
            for(int j=i*rows_per_group; j<(i+1)*rows_per_group; j++){

                // Sum of all ocurrences
                for(int l=0; l<real_output[j].length; l++){
                    matrix[i][l] += real_output[j][l];
                    total += real_output[j][l];
                }
            }

            // Mean of all ocurrences
            for(int l=0; l<real_output[i].length; l++){
                matrix[i][l] = matrix[i][l]/total;
            }
        }

        return matrix;
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


        // Reading dataset
        DataTuple data_tuple =
                read_class_dataset(
                        "C:/Users/VictorStefano/IdeaProjects/CC5114/src/tarea1/iris.data", ",");
        double[][] data = normalize_data(data_tuple.getInputs(), 1.0, 0.0);
        double[][] target = data_tuple.getOutput();

        // Splitting training data
        double[][] train_data = new double[90][4];
        double[][] train_target = new double[90][3];
        for(int i=0; i<90; i++){
            train_data[i] = data[i];
            train_target[i] = target[i];
        }

        // Splitting evaluation data
        double[][] eval_data = new double[60][4];
        double[][] eval_target = new double[60][3];
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
                                "mse"
                        )
                }
        );

        // Train and Eval
        n.train(train_data, train_target, 100000);
        DataTuple dt = n.eval(eval_data, eval_target);
        double[][] eval_output = dt.getOutput();

        // Confusion matrix
        double[][] conf_matrix = confusion_matrix(eval_output);

        // Print Confusion matrix
        for(int i=0; i<conf_matrix.length; i++){
            System.out.print("[");
            for(int j=0; j<conf_matrix[i].length; j++){
                System.out.print("   " + numberFormat.format(conf_matrix[i][j]) + "   ");
            }
            System.out.print(" ]\n");
        }
    }
}
