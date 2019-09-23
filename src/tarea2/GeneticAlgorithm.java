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

    public GeneticAlgorithm(double mutation_rate, double selection_rate, int population_size,
                            int number_of_gens, Population population, FitFun fun){
        this.mutation_rate = mutation_rate;
        this.selection_rate = selection_rate;
        this.population_size = population_size;
        this.number_of_gens = number_of_gens;
        this.fitfun = fun;
        population.initPopulation(population_size, number_of_gens);
        this.population = population;
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
