package tarea1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class DataUtils {

    public static Tuple generate_random_binary_dataset(int rows, double m, double b){
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
        return new Tuple(dataset, output);
    }

    public static Tuple read_class_dataset(String file, String separator){

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
        return new Tuple(data, target);
    }

    public static double[][] normalize_data(double[][] data, double nh, double nl){

        // d_low and d_high per column
        double d_low[] = new double[data[0].length];
        double d_high[] = new double[data[0].length]; ;
        Arrays.fill(d_low, Double.MAX_VALUE);
        Arrays.fill(d_high, Double.MIN_VALUE);

        // Getting max and min per column
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                if(data[i][j] < d_low[j]){ d_low[j] = data[i][j]; }
                if(data[i][j] > d_high[j]){ d_high[j] = data[i][j]; }
            }
        }

        // Normalizing data per columns
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                data[i][j] = (((data[i][j]-d_low[j])*(nh-nl))/(d_high[j]-d_low[j])) + nl;
            }
        }
        return data;
    }

    public static double[][] confusion_matrix(double[][] real_output){

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

    public static Tuple[] separate_train_and_eval_data(double[][] data, double[][] target, double percentage){

        int total_rows = data.length;
        int n_classes = target[0].length;
        int rows_per_class = total_rows/n_classes;

        int train_rows = (int)(total_rows*percentage);
        int eval_rows = total_rows - train_rows;

        double[][] train_data = new double[train_rows][data[0].length];
        double[][] train_target = new double[train_rows][target[0].length];

        double[][] eval_data = new double[eval_rows][data[0].length];
        double[][] eval_target = new double[eval_rows][target[0].length];

        // Getting train and eval data
        int t = 0;
        int e = 0;
        for(int i=0; i<n_classes; i++){

            // Training Data
            for(int j=i*rows_per_class; j<i*rows_per_class+train_rows/n_classes; j++){
                train_data[t] = data[j];
                train_target[t] = target[j];
                t++;
            }

            // Eval Data
            for(int j=i*rows_per_class+train_rows/n_classes;
                j<i*rows_per_class+(train_rows + eval_rows)/n_classes; j++){

                eval_data[e] = data[j];
                eval_target[e] = target[j];
                e++;
            }
        }

        return new Tuple[]{new Tuple(train_data, train_target), new Tuple(eval_data, eval_target)};
    }

    public static double[][] transpose(double[][] matrix){
        // Transposing
        double[][] aux = new double[matrix[0].length][matrix.length];
        for(int j=0; j<matrix[0].length; j++){
            for(int k=0; k<matrix.length; k++){
                aux[j][k] = matrix[k][j];
            }
        }
        return aux;
    }

    public static NeuralNetwork read_net_from_serializable(String path){
        try {

            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            objectIn.close();
            return (NeuralNetwork) obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
