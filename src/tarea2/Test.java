package tarea2;

import javafx.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tarea1.LinePlot;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        PopulationFactory fac = (PopulationFactory) context.getBean("populationFactory");
        GeneticAlgorithm ga = new GeneticAlgorithm(
                0.5,
                0.5,
                1000,
                3,
                false,
                false,
                fac.createStringPopulation(),
                new ArrayList<Pair<FitFun, Double>>(){
                    {
                        add(new Pair<FitFun, Double>(
                                individual -> {
                                    String[] ind = (String[]) individual;
                                    String[] answer = new String[]{"c","a","t"};
                                    double score = 0.0;
                                    for(int i=0; i<answer.length; i++){
                                        if(ind[i].equals(answer[i])){
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
        double[][] fitnessess = ga.solve(3);
        LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
        chart1.show_plot();
    }
}
