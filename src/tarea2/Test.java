package tarea2;

import javafx.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
                true,
                true,
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
        ga.solve(3);
    }
}
