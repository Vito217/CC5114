package tarea2;

import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm{

    private double mutation_rate;
    private double selection_rate;
    private int population_size;
    private int number_of_gens;
    private FitFun fitfun;
    private Population population;

    public GeneticAlgorithm(double mr, double sr, int ps, int ng, Population p, FitFun f){
        mutation_rate = mr;
        selection_rate = sr;
        population_size = ps;
        number_of_gens = ng;
        fitfun = f;
        p.initPopulation(ps, ng);
        population = p;
    }

    // SELECTION
    public Object[][] tournament() {
        return population.tournament(selection_rate, fitfun);
    }

    // GENETIC OPERATORS
    public Object[][] crossover(Object[][] selected){
        return population.crossover(mutation_rate, selected);
    }

    public void solve(int iterations){
        for(int i=0; i<iterations; i++){
            population.printPopulation();
            Object[][] selected = tournament();
            Object[][] winners = crossover(selected);
        }
    }
}
