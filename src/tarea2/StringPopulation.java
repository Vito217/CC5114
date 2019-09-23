package tarea2;

import javafx.util.Pair;
import java.util.*;

public class StringPopulation implements Population{

    private String[][] population;
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz";

    public StringPopulation(){}

    @Override
    public void initPopulation(int ps, int ng){
        population = new String[ps][ng];
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<ps; i++){
            for(int j=0; j<ng; j++){
                int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
                builder.append(ALPHA_NUMERIC_STRING.charAt(character));
                population[i][j] = builder.toString();
                builder.deleteCharAt(0);
            }
        }
    }

    @Override
    public String[][] tournament(double selection_rate, FitFun fit) {
        Random rnd = new Random();
        String[][] selected_population = new String[2 * population.length][population[0].length];
        int n_select = (int) Math.round(population.length * selection_rate);

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.length; i++) {
            ArrayList<Pair<String[], Integer>> list = new ArrayList<>();
            // We select n_select members
            for (int j = 0; j < n_select; j++) {
                String[] rival = population[rnd.nextInt(population.length)];
                list.add(new Pair<>(rival, (Integer) fit.fitness(rival)));
            }
            // We sort and get the best
            list.sort(Comparator.comparing(Pair::getValue));
            selected_population[i] = list.get(list.size()-1).getKey();
        }

        return selected_population;
    }

    @Override
    public String[][] crossover(double mutation_rate, Object[][] selected) {
        String[][] sel = (String[][]) selected;
        int first_num_gens = (int) Math.round(sel[0].length * mutation_rate);
        for(int i=0; i<sel.length; i += 2){
            int pop_index = i/2;
            String[] father = sel[i];
            String[] mother = sel[i+1];
            String[] son = new String[sel[i].length];
            for(int j=0; j<sel[i].length; j++){
                if(j<=first_num_gens){
                    son[j] = mother[j];
                }
                else{
                    son[j] = father[j];
                }
            }
            population[pop_index] = son;
        }
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

}
