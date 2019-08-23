package tarea1;

import java.text.DecimalFormat;

public class Test {

    public static void main(String[] args){

        // Reading dataset
        Tuple data_tuple =
                DataUtils.read_class_dataset(
                        "C:/Users/VictorStefano/IdeaProjects/CC5114/src/tarea1/iris.data",
                        ",");

        double[][] data = DataUtils.normalize_data((double[][]) data_tuple.getFirst(),1.0,0.0);
        double[][] target = (double[][]) data_tuple.getSecond();

        // Splitting training data
        Tuple[] train_and_eval = DataUtils.separate_train_and_eval_data(data, target, 0.8);

        double[][] train_data = (double[][]) train_and_eval[0].getFirst();
        double[][] train_target = (double[][]) train_and_eval[0].getSecond();
        double[][] eval_data = (double[][]) train_and_eval[1].getFirst();
        double[][] eval_target = (double[][]) train_and_eval[1].getSecond();

        NeuralNetwork n = new NeuralNetwork(
                new Layer[]{
                        new Layer(
                                4,
                                4,
                                0.1,
                                "sigmoid",
                                "mse"
                        ),
                        new Layer(
                                4,
                                3,
                                0.1,
                                "sigmoid",
                                "mse"
                        )
                }
        );

        // Train and Eval
        Tuple loss_success = n.train(train_data, train_target, 50000);
        Tuple dt = n.eval(eval_data, eval_target);

        // Results
        double[] loss = (double[]) loss_success.getFirst();
        double[] success = (double[]) loss_success.getSecond();
        double[][] eval_output = (double[][]) dt.getSecond();

        // Confusion matrix
        double[][] conf_matrix = DataUtils.confusion_matrix(eval_output);

        // Print Confusion matrix
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        for(int i=0; i<conf_matrix.length; i++){
            System.out.print("[");
            for(int j=0; j<conf_matrix[i].length; j++){
                System.out.print("   " + numberFormat.format(conf_matrix[i][j]) + "   ");
            }
            System.out.print(" ]\n");
        }

        // Plotting
        LinePlot chart = new LinePlot("Loss v/s Iterations", loss);
        chart.show_plot();

        LinePlot chart2 = new LinePlot("Right Answers v/s Iterations", success);
        chart2.show_plot();
    }
}
