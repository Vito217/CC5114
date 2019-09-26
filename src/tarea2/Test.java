package tarea2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        PopulationFactory fac = (PopulationFactory) context.getBean("populationFactory");

        GeneticAlgorithm ga = new GeneticAlgorithm(0.5,
                                                   0.5,
                                                   1000,
                                                   3,
                                                   false,
                                                   fac.createStringPopulation(),
                individual -> {
                    String[] ind = (String[]) individual;
                    String[] answer = new String[]{"c","a","t"};
                    int score = 0;
                    for(int i=0; i<answer.length; i++){
                        if(ind[i].equals(answer[i])){
                            score++;
                        }
                    }
                    return score;
                });

        ga.solve(3);
    }
}
