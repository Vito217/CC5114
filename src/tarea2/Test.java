package tarea2;

import javafx.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tc33.jheatchart.HeatChart;
import tarea1.LinePlot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Test {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        PopulationFactory fac = (PopulationFactory) context.getBean("populationFactory");

        /**
         // ----------------------------Testing finding a phrase--------------------------------------------------
        GeneticAlgorithm ga = new GeneticAlgorithm(
                0.1,
                1000,
                10,
                "crossover",
                false,
                false,
                fac.createStringPopulation(),
                new ArrayList<Pair<FitFun, Double>>(){
                    {
                        add(new Pair<FitFun, Double>(
                                individual -> {
                                    String[] ind = (String[]) individual;
                                    String[] answer = new String[]{"h","e","l","l","o","w","o","r","l","d"};
                                    double score = 0.0;
                                    for(int i=0; i<answer.length; i++){
                                        if(answer[i].equalsIgnoreCase(ind[i])){
                                            score++;
                                        }
                                    }
                                    return score;
                                },
                                0.0
                        ));
                    }
                }
        );

        //double[][] fitnessess = ga.solve(20);
        //LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
        //chart1.show_plot();

        ga.get_heatmap(
                new int[]{50, 1000},
                new double[]{0.0, 1.0},
                50,
                0.1,
                "mutation",
                20
        );
        **/


        /**
        // ----------------------------Testing finding a secuence--------------------------------------------------
        GeneticAlgorithm ga = new GeneticAlgorithm(
                0.2,
                1000,
                14,
                "mutation",
                false,
                false,
                fac.createBinaryPopulation(),
                new ArrayList<Pair<FitFun, Double>>(){
                    {
                        add(new Pair<FitFun, Double>(
                                individual -> {
                                    Integer[] ind = (Integer[]) individual;
                                    Integer[] answer = new Integer[]{0,0,1,0,1,0,1,0,1,1,0,1,0,1};
                                    double score = 0.0;
                                    for(int i=0; i<answer.length; i++){
                                        if(ind[i] != null && answer[i].equals(ind[i])){
                                            score++;
                                        }
                                    }
                                    return score;
                                },
                                0.0
                        ));
                    }
                }
        );

        double[][] fitnessess = ga.solve(20);
        LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
        chart1.show_plot();


        ga.get_heatmap(
                new int[]{50, 1000},
                new double[]{0.0, 1.0},
                50,
                0.1,
                "mutation",
                20
        );
        **/

        //----------------------------Unbound Knapsack--------------------------------------------------
        GeneticAlgorithm ga = new GeneticAlgorithm(
                0.2,
                10,
                5,
                "mutation",
                false,
                false,
                fac.createUBKPopulation(),
                new ArrayList<Pair<FitFun, Double>>(){
                    {
                        add(new Pair<FitFun, Double>(
                                individual -> {
                                    int[] weights = new int[individual.length];
                                    int[] values = new int[individual.length];
                                    int total_weight = 0;
                                    double score = 0.0;
                                    for(int i=0; i<individual.length; i++){
                                        String ind = (String) individual[i];
                                        if(ind != null){
                                            String[] pair = ind.split("/");
                                            weights[i] = Integer.parseInt(pair[0]);
                                            values[i] = Integer.parseInt(pair[1]);
                                        }
                                        else{
                                            weights[i] = 0;
                                            values[i] = 0;
                                        }
                                    }
                                    for(int i=0; i<individual.length; i++){
                                        total_weight += weights[i];
                                        if(total_weight < 15){
                                            score += values[i];
                                        }
                                    }
                                    return score;
                                },
                                0.0
                        ));
                    }
                }
        );

        //double[][] fitnessess = ga.solve(20);
        //LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
        //chart1.show_plot();


        ga.get_heatmap(
                new int[]{50, 1000},
                new double[]{0.0, 1.0},
                50,
                0.1,
                "mutation",
                20
        );

    }
}
