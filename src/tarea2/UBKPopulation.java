package tarea2;

import javafx.util.Pair;
import tarea1.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class UBKPopulation extends Population{

    private String[][] population;
    private String[] combinations = new String[]{"12/4", "2/2", "1/2", "1/1", "4/10"};

    public UBKPopulation(){ }

    @Override
    void initPopulation(int ps, int ng) {
        population = new String[ps][ng];
        for(int i=0; i<ps; i++){
            for(int j=0; j<ng; j++){
                population[i][j] = combinations[rand.nextInt(combinations.length)];
            }
        }
    }

    @Override
    public Tuple tournament(ArrayList<Pair<FitFun, Double>> fitfun, boolean pareto) {
        if(pareto){
            return pareto_tournament(fitfun);
        }
        else{
            return normal_tournament(fitfun);
        }
    }

    @Override
    public String[][] crossover(double mutation_rate, Tuple selected, boolean elitist) {
        String[][] sel = (String[][]) selected.getFirst();
        String[] best = (String[]) selected.getSecond();
        for(int i=0; i<sel.length; i += 2){
            int pop_index = i/2;
            String[] father = sel[i];
            String[] mother = sel[i+1];
            String[] son = new String[sel[i].length];
            int random = Math.round(rand.nextFloat());
            int first_num_gens = rand.nextInt(sel[i].length);
            if(random == 0){
                String[] aux = father;
                father = mother;
                mother = aux;
            }
            for(int j=0; j<sel[i].length; j++){
                if(j<=first_num_gens){ son[j] = mother[j]; }
                else{ son[j] = father[j]; }
            }
            population[pop_index] = son;
        }
        if(elitist){ population[0] = best; }
        return population;
    }

    @Override
    public String[][] mutation(double mutation_rate, Tuple selected, boolean elitist) {
        String[][] sel = (String[][]) selected.getFirst();
        String[] best = (String[]) selected.getSecond();
        int number_of_mutations = (int) Math.round(sel[0].length* mutation_rate);

        // For each selected individual
        for(int i=0; i<sel.length/2; i++){

            //  We alter the number of genes acordding to mutation rate
            for(int j=0; j<number_of_mutations; j++){
                sel[i][rand.nextInt(sel[i].length)] = combinations[rand.nextInt(combinations.length)];
            }

            population[i] = sel[i];
        }

        if(elitist){ population[0] = best; }
        return population;
    }

    @Override
    public String[][] getPopulation(){
        return population;
    }

    @Override
    public void printPopulation() {
        for(int i=0; i<population.length; i++){
            System.out.println(Arrays.toString(population[i]));
        }
    }

    private boolean paretoConditionOne(Double[] fit1, Double[] fit2){
        for(int i = 0; i < fit1.length; i++){
            if(fit1[i] < fit2[i]) { return false; }
        }
        return true;
    }

    private boolean paretoConditionTwo(Double[] fit1, Double[] fit2){
        for(int i = 0; i < fit1.length; i++){
            if(fit1[i] > fit2[i]) { return true; }
        }
        return false;
    }

    private Tuple pareto_tournament(ArrayList<Pair<FitFun, Double>> fitfun){

        // We sort fitness functions by priority
        fitfun.sort(Comparator.comparing(Pair::getValue));

        String[][] selected_population = new String[2 * population.length][population[0].length];
        String[] best_individual = new String[population[0].length];
        int n_select = (int) Math.round(population.length * rand.nextFloat());

        Double[] actual_best_fitness = new Double[fitfun.size()];
        Arrays.fill(actual_best_fitness, Double.MIN_VALUE);

        String[][] rivals = new String[n_select][population[0].length];
        Double[][] fitnesses = new Double[n_select][fitfun.size()+1];

        double global_best_fitness = Double.MAX_VALUE;
        double global_average_fitness = 0;
        double global_worst_fitness = Double.MAX_VALUE;

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.length; i++) {

            // We select n_select members

            // Getting random rivals
            for (int j = 0; j < n_select; j++) {

                rivals[j] = population[rand.nextInt(population.length)];

                // We compute fitness for each subfitness function
                for(int k = 0; k < fitfun.size(); k++){
                    fitnesses[j][k] = fitfun.get(k).getKey().fitness(rivals[j]);
                    global_average_fitness += fitnesses[j][k]/(2*population.length*n_select*fitfun.size());
                    if(fitnesses[j][k] > global_best_fitness){
                        global_best_fitness = fitnesses[j][k];
                    }
                    if(fitnesses[j][k] < global_worst_fitness){
                        global_worst_fitness = fitnesses[j][k];
                    }
                }
                //Last value corresponds to rival's index
                fitnesses[j][fitfun.size()] = (double) j;
            }

            // We sort fitnesses by priotrity
            for(int j = 0; j < fitfun.size(); j++){
                final int ind = j;
                Arrays.sort(fitnesses, (fit1, fit2) -> {
                    if (fit1[ind] < fit2[ind])
                        return 1;
                    else if (fit1[ind].equals(fit2[ind]))
                        return 0;
                    else
                        return -1;
                });
            }

            Double[] best_fitness = Arrays.copyOfRange(fitnesses[0], 0, fitfun.size());
            int index_of_best = (int) Math.round(fitnesses[0][fitfun.size()]);
            selected_population[i] = rivals[index_of_best];

            if(paretoConditionOne(best_fitness, actual_best_fitness) &&
                    paretoConditionTwo(best_fitness, actual_best_fitness)){
                actual_best_fitness = best_fitness;
                best_individual = rivals[index_of_best];
            }
        }

        return new Tuple(selected_population, best_individual, new Double[]{global_worst_fitness,
                global_average_fitness, global_best_fitness});
    }

    private Tuple normal_tournament(ArrayList<Pair<FitFun, Double>> fitfun) {

        fitfun.sort(Comparator.comparing(Pair::getValue));

        String[][] selected_population = new String[2 * population.length][population[0].length];
        String[] best_individual = new String[population[0].length];
        int n_select = (int) Math.round(population.length * rand.nextFloat());

        double global_best_fitness = Double.MIN_VALUE;
        double global_average_fitness = 0;
        double global_worst_fitness = Double.MAX_VALUE;

        Double[] fitnesses = new Double[fitfun.size()];

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.length; i++) {

            // Getting random rivals
            double local_best_fit = Double.MIN_VALUE;

            for (int j = 0; j < n_select; j++) {

                String[] rival = population[rand.nextInt(population.length)];

                // We compute fitness for each subfitness function
                for(int k = 0; k < fitfun.size(); k++){
                    fitnesses[k] = fitfun.get(k).getKey().fitness(rival);
                    global_average_fitness += fitnesses[k]/(2*population.length*n_select*fitfun.size());
                }

                // Not pareto implies we keep the best fit by default
                Arrays.sort(fitnesses);
                double max_fit = fitnesses[fitnesses.length-1];
                double min_fit = fitnesses[0];

                // We check and save the best winner of the tournament
                if(max_fit > local_best_fit){
                    local_best_fit = max_fit;
                    selected_population[i] = rival;
                }
                if(max_fit > global_best_fitness){
                    global_best_fitness = max_fit;
                    best_individual = rival;
                }
                if(min_fit < global_worst_fitness){
                    global_worst_fitness = min_fit;
                }
            }
        }

        return new Tuple(selected_population, best_individual, new Double[]{global_worst_fitness,
                global_average_fitness, global_best_fitness});
    }
}
