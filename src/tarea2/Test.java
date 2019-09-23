package tarea2;
import javafx.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class Test {

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        PopulationFactory fac = (PopulationFactory) context.getBean("populationFactory");

        GeneticAlgorithm ga = new GeneticAlgorithm(0.5,
                                                   0.5,
                                                   100,
                                                   3,
                                                   fac.createStringPopulation(),
                individual -> {
                    String[] ind = (String[]) individual;
                    String[] answer = new String[]{"c", "a", "t"};
                    int score = 0;
                    for(int i=0; i<answer.length; i++){
                        if(ind[i].equals(answer[i])){
                            score++;
                        }
                    }
                    return score;
                });

        ga.solve(100);
    }
}
