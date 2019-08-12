package tarea1;

import java.util.Random;
import org.jfree.ui.RefineryUtilities;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Test {

    public static double[][] generate_random_dataset(int rows, int cols){
        Random r = new Random();
        double[][] dataset = new double[rows][cols];
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                dataset[i][j] = r.nextDouble();
            }
        }
        return dataset;
    }

    public static void main(String[] args){

        double[][] train_data = new double[10][3];
        train_data[0] = new double[]{1,2,1};
        train_data[1] = new double[]{1,4,1};
        train_data[2] = new double[]{4,4,1};
        train_data[3] = new double[]{1,8,1};
        train_data[4] = new double[]{5,7,1};
        train_data[5] = new double[]{5,1,0};
        train_data[6] = new double[]{7,2,0};
        train_data[7] = new double[]{8,2,0};
        train_data[8] = new double[]{7,4,0};
        train_data[9] = new double[]{3,1,0};

        double[][] eval_data = new double[10][3];
        eval_data[0] = new double[]{1.5,7.5,1};
        eval_data[1] = new double[]{3.5,6.5,1};
        eval_data[2] = new double[]{2.5,2.6,1};
        eval_data[3] = new double[]{3.2,3.3,1};
        eval_data[4] = new double[]{2.8,3.0,1};
        eval_data[5] = new double[]{5.2,1.1,0};
        eval_data[6] = new double[]{8.9,8.8,0};
        eval_data[7] = new double[]{4.7,4.5,0};
        eval_data[8] = new double[]{1.9,1.4,0};
        eval_data[9] = new double[]{3.6,2.5,0};

        Perceptron p = new Perceptron(2);

        p.train("sigmoid", 10, train_data);
        double[][] dt = p.evaluate("sigmoid", eval_data);

        SwingUtilities.invokeLater(() -> {
            MyPlot example = new MyPlot("Title", dt);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

        System.out.println("El test paso correctamente");
    }
}
