package tarea2;

import tarea1.Tuple;
import javafx.util.Pair;
import java.util.*;

public class StringPopulation implements Population{

    private String[][] population;
    private Random rand = new Random();
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz ";

    public StringPopulation(){}

    @Override
    public void initPopulation(int ps, int ng){
        population = new String[ps][ng];
        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();
        for(int i=0; i<ps; i++){
            for(int j=0; j<ng; j++){
                int character = (int)(rnd.nextFloat()*ALPHA_NUMERIC_STRING.length());
                builder.append(ALPHA_NUMERIC_STRING.charAt(character));
                population[i][j] = builder.toString();
                builder.deleteCharAt(0);
            }
        }
    }

    @Override
    public Tuple tournament(double selection_rate, ArrayList<Pair<FitFun, Double>> fitfun, boolean pareto) {
        if(pareto){
            return pareto_tournament(selection_rate, fitfun);
        }
        else{
            return normal_tournament(selection_rate, fitfun);
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

    private Tuple pareto_tournament(double selection_rate, ArrayList<Pair<FitFun, Double>> fitfun){

        // We sort fitness functions by priority
        fitfun.sort(Comparator.comparing(Pair::getValue));

        String[][] selected_population = new String[2 * population.length][population[0].length];
        String[] best_individual = new String[population[0].length];
        int n_select = (int) Math.round(population.length * selection_rate);

        Double[] actual_best_fitness = new Double[fitfun.size()];
        Arrays.fill(actual_best_fitness, Double.MIN_VALUE);

        String[][] rivals = new String[n_select][population[0].length];
        Double[][] fitnesses = new Double[n_select][fitfun.size()+1];

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.length; i++) {

            // We select n_select members

            // Getting random rivals
            for (int j = 0; j < n_select; j++) {

                rivals[j] = population[rand.nextInt(population.length)];

                // We compute fitness for each subfitness function
                for(int k = 0; k < fitfun.size(); k++){
                    fitnesses[j][k] = fitfun.get(k).getKey().fitness(rivals[j]);
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

        return new Tuple(selected_population, best_individual, actual_best_fitness);
    }

    private Tuple normal_tournament(double selection_rate, ArrayList<Pair<FitFun, Double>> fitfun) {

        fitfun.sort(Comparator.comparing(Pair::getValue));

        String[][] selected_population = new String[2 * population.length][population[0].length];
        String[] best_individual = new String[population[0].length];
        int n_select = (int) Math.round(population.length * selection_rate);

        double global_best_fitness = Double.MIN_VALUE;
        Double[] fitnesses = new Double[fitfun.size()];

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.length; i++) {

            // Getting random rivals
            String[] local_best_rival = new String[population[0].length];
            double local_best_fit = Double.MIN_VALUE;

            for (int j = 0; j < n_select; j++) {

                String[] rival = population[rand.nextInt(population.length)];

                // We compute fitness for each subfitness function
                for(int k = 0; k < fitfun.size(); k++){
                    fitnesses[k] = fitfun.get(k).getKey().fitness(rival);
                }

                // Not pareto implies we keep the best fit by default
                Arrays.sort(fitnesses);
                double max_fit = fitnesses[fitnesses.length-1];
                if(max_fit > local_best_fit){
                    local_best_fit = max_fit;
                    local_best_rival = rival;
                }
            }
            selected_population[i] = local_best_rival;
            if(local_best_fit > global_best_fitness){
                global_best_fitness = local_best_fit;
                best_individual = local_best_rival;
            }
        }

        return new Tuple(selected_population, best_individual, fitnesses);
    }
}
