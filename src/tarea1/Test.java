package tarea1;

public class Test {

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
