package tarea1;

import java.util.Random;
import org.jfree.ui.RefineryUtilities;

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

        Perceptron and_gate = new AndGate();
        Perceptron or_gate = new OrGate();
        Perceptron nand_gate = new NandGate();

        double[] set_1 = {1,1};
        double[] set_2 = {1,0};
        double[] set_3 = {0,1};
        double[] set_4 = {0,0};

        assert and_gate.evaluate(set_1) == 1;
        assert and_gate.evaluate(set_2) == 0;
        assert and_gate.evaluate(set_3) == 0;
        assert and_gate.evaluate(set_4) == 0;

        assert or_gate.evaluate(set_1) == 1;
        assert or_gate.evaluate(set_2) == 1;
        assert or_gate.evaluate(set_3) == 1;
        assert or_gate.evaluate(set_4) == 0;

        assert nand_gate.evaluate(set_1) == 0;
        assert nand_gate.evaluate(set_2) == 1;
        assert nand_gate.evaluate(set_3) == 1;
        assert nand_gate.evaluate(set_4) == 1;

        System.out.println("El test paso correctamente");
    }
}
