package tarea2;

import javafx.util.Pair;
import tarea1.Tuple;

import java.util.ArrayList;

public class GeneticAlgorithm{

    private double mutation_rate;
    private double selection_rate;
    private int population_size;
    private int number_of_gens;
    private ArrayList<Pair<FitFun, Double>> fitfun;
    private Population population;
    private boolean elitist;
    private boolean pareto;
    private int[] priorities;

    public GeneticAlgorithm(double mutation_rate, double selection_rate, int population_size,
                            int number_of_gens, boolean elitist, boolean pareto, Population population,
                            ArrayList<Pair<FitFun, Double>> fitfun){
        this.mutation_rate = mutation_rate;
        this.selection_rate = selection_rate;
        this.population_size = population_size;
        this.number_of_gens = number_of_gens;
        this.fitfun = fitfun;
        population.initPopulation(population_size, number_of_gens);
        this.population = population;
        this.elitist = elitist;
        this.pareto = pareto;
    }

    // SELECTION
    public Tuple tournament() {
        return population.tournament(selection_rate, fitfun, pareto);
    }

    // GENETIC OPERATORS
    public Object[][] crossover(Tuple selected){
        return population.crossover(mutation_rate, selected, elitist);
    }

    public void solve(int iterations){
        for(int i=0; i<iterations; i++){
            population.printPopulation();
            Tuple selected = tournament();
            Object[][] winners = crossover(selected);
        }
    }
}
