package tarea3;

import javafx.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tarea1.LinePlot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        AddNodeFactory addFact = (AddNodeFactory) context.getBean("addNodeFactory");
        SubNodeFactory subFact = (SubNodeFactory) context.getBean("subNodeFactory");
        MultNodeFactory multFact = (MultNodeFactory) context.getBean("multNodeFactory");
        MaxNodeFactory maxFact = (MaxNodeFactory) context.getBean("maxNodeFactory");
        DivNodeFactory divFact = (DivNodeFactory) context.getBean("divNodeFactory");

        ArrayList<NodeFactory> allowed_functions = new ArrayList<NodeFactory>(){
            {
                add(addFact);
                add(subFact);
                add(multFact);
            }
        };

        ArrayList<Object> allowed_terminals = new ArrayList<Object>(){
            {
                add(-10);
                add(-9);
                add(-8);
                add(-7);
                add(-6);
                add(-5);
                add(-4);
                add(-3);
                add(-2);
                add(-1);
                add(0);
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
                add(7);
                add(8);
                add(9);
                add(10);
                add("x");
            }
        };

        ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
            add(new Pair<GPFitFun, Double>(
                    tree -> {
                        // Encontrar funcion x*x + x - 6
                        int distance = Integer.MAX_VALUE;
                        if(tree != null){
                            int[] input_range = new int[]{-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10};
                            int[] target_range = new int[]{84,66,50,36,24,14,6,0,-4,-6,-6,-4,0,6,14,24,36,50,66,84,104};
                            double mean_distance = 0.0;
                            HashMap<Object, Integer> vars = new HashMap<>();
                            vars.put("x", 0);
                            for(int i=0; i<input_range.length; i++){
                                vars.put("x", input_range[i]);
                                try{
                                    int res = (int) tree.eval(vars);
                                    mean_distance += Math.abs(res - target_range[i]) / (double) input_range.length;
                                }
                                catch (ClassCastException e){
                                    mean_distance = Integer.MAX_VALUE;
                                    break;
                                }
                            }
                            distance = (int) Math.round(mean_distance);
                        }
                        return distance == 0 ? 1 : 1.0/distance;
                    },
                    0.0
            ));
        }};

        GeneticProgram gp = new GeneticProgram(
              1000,
              0.1,
              "mutation",
              true,
               fitfuns,
              10,
              0.4f,
               allowed_functions,
               allowed_terminals
        );

        double[][] fitnessess = gp.solve(20);
        LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
        chart1.show_plot();

        //gp.get_heatmap(
        //        new int[]{50, 1000},
        //        new double[]{0.0, 1.0},
        //        50,
        //        0.1,
        //        "mutation",
        //        20
        //);
    }

}
