package tarea3;
import javafx.util.Pair;
import org.tc33.jheatchart.HeatChart;
import tarea1.DataUtils;
import tarea1.Tuple;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneticProgram {

    private ArrayList<Node> population;
    private AST tree_generator;
    private ArrayList<Pair<GPFitFun, Double>> fitfun;
    private String reproduction;
    private int max_depth;
    private Random rand;
    private boolean elitist;
    private double mutation_rate;

    public GeneticProgram(
            int population_size,
            double mut_rate,
            String rep,
            boolean elit,
            ArrayList<Pair<GPFitFun, Double>> ff,
            int depth,
            float prob_terminal,
            ArrayList<NodeFactory> functions,
            ArrayList<Object> terminals
    ) {
        population = new ArrayList<>();
        fitfun = ff;
        tree_generator = new AST(functions, terminals, prob_terminal);
        reproduction = rep;
        max_depth = depth;
        rand = new Random(42);
        elitist = elit;
        mutation_rate = mut_rate;
        for(int i=0; i<population_size; i++){
            population.add(tree_generator.createRecTree(max_depth));
        }
    }

    private Tuple tournament() {

        fitfun.sort(Comparator.comparing(Pair::getValue));

        ArrayList<Node> selected_population = new ArrayList<>();
        Node best_individual = null;

        double global_best_fitness = Double.MIN_VALUE;
        double global_average_fitness = 0;
        double global_worst_fitness = Double.MAX_VALUE;

        Double[] fitnesses = new Double[fitfun.size()];

        // Retrieving 2N selected individuals
        for (int i = 0; i < 2 * population.size(); i++) {

            // Getting random rivals
            double local_best_fit = Double.MIN_VALUE;
            Node local_best_rival = population.get(rand.nextInt(population.size()));
            int n_select = Math.round(population.size() * rand.nextFloat());

            for (int j = 0; j < n_select; j++) {

                Node rival = population.get(rand.nextInt(population.size()));

                // We compute fitness for each subfitness function
                for (int k = 0; k < fitfun.size(); k++) {
                    fitnesses[k] = fitfun.get(k).getKey().fitness(rival);
                    global_average_fitness += fitnesses[k] / (2 * population.size() * n_select * fitfun.size());
                }

                // Not pareto implies we keep the best fit by default
                Arrays.sort(fitnesses);
                double max_fit = fitnesses[fitnesses.length - 1];
                double min_fit = fitnesses[0];

                // We check and save the best winner of the tournament
                if (max_fit > local_best_fit) {
                    local_best_fit = max_fit;
                    local_best_rival = rival;
                }
                if (max_fit > global_best_fitness) {
                    global_best_fitness = max_fit;
                    best_individual = rival;
                }
                if (min_fit < global_worst_fitness) {
                    global_worst_fitness = min_fit;
                }
            }
            selected_population.add(local_best_rival);
        }

        return new Tuple(
                selected_population,
                best_individual,
                new Double[]{global_worst_fitness, global_average_fitness, global_best_fitness});

    }

    public void crossover(Tuple selected) {
        ArrayList<Node> sel = (ArrayList<Node>) selected.getFirst();
        Node best = (Node) selected.getSecond();
        for(int i=0; i<2*population.size(); i += 2){
            int population_index = i/2;
            Node father = sel.get(i);
            Node mother = sel.get(i+1);
            int random = Math.round(rand.nextFloat());
            if(random == 0){
                Node aux = father;
                father = mother;
                mother = aux;
            }
            ArrayList<Node> father_serial = father.serialize();
            ArrayList<Node> mother_serial = mother.serialize();
            int father_rand_ind = rand.nextInt(father_serial.size());
            int mother_rand_ind = rand.nextInt(mother_serial.size());
            Node node_to_replace = father_serial.get(father_rand_ind);
            Node node_replacing = mother_serial.get(mother_rand_ind);
            if(node_to_replace.getParent() == null){
                father = node_replacing;
            }
            else {
                node_to_replace.replace(node_replacing);
            }
            population.set(population_index, father);
        }
        if(elitist) {
            population.set(0, best);
        }
    }

    public void mutation(Tuple selected, double mutation_rate) {
        ArrayList<Node> sel = (ArrayList<Node>) selected.getFirst();
        Node best = (Node) selected.getSecond();
        // For each selected individual
        for(int i=0; i<population.size()/2; i++){
            Node tree = sel.get(i);
            ArrayList<Node> tree_serial = tree.serialize();
            int number_of_mutations = (int) Math.round(tree_serial.size() * mutation_rate);
            //  We alter the number of genes acordding to mutation rate
            for(int j=0; j<number_of_mutations; j++){
                int rand_ind = rand.nextInt(tree_serial.size());
                Node n = tree_serial.get(rand_ind);
                if(n.getParent() == null){
                    tree = tree_generator.createRecTree(rand.nextInt(max_depth));
                }
                else {
                    n.replace(tree_generator.createRecTree(rand.nextInt(max_depth)));
                }
            }
            population.set(i, tree);
        }
        if(elitist){
            population.set(0, best);
        }
    }

    public double[][] solve(int iterations){
        Double[][] fit_per_gen = new Double[iterations][fitfun.size()];
        for(int i=0; i<iterations; i++){
            System.out.println("GEN "+i);
            Tuple selected = tournament();
            fit_per_gen[i] = (Double[]) selected.getThird();
            if(reproduction.equals("crossover")){
                crossover(selected);
            }
            else{
                mutation(selected, mutation_rate);
            }
        }

        return DataUtils.transpose(DataUtils.unbox(fit_per_gen));
    }

    public void get_heatmap(int[] population_range, double[] mutation_range,
                            int population_step, double mutation_step,
                            String reproduction_op, int generations) throws IOException {

        System.out.println("Computing Heat Map using "+reproduction_op+" and "+generations+" generations");

        ArrayList<Node> aux = population;
        Double[][] map = new Double[(population_range[1]-population_range[0])/population_step + 1]
                [(int)((mutation_range[1]-mutation_range[0])/mutation_step) + 1];

        Integer[] y_axis = new Integer[(population_range[1]-population_range[0])/population_step + 1];
        Double[] x_axis = new Double[(int)((mutation_range[1]-mutation_range[0])/mutation_step) + 1];

        int ind_i = 0;
        int ind_j = 0;
        for(int i=population_range[0]; i<=population_range[1]; i += population_step){
            y_axis[ind_i] = i;
            for(double j=mutation_range[0]; j<=mutation_range[1]; j = Math.round((j + mutation_step)*10d)/10d){
                x_axis[ind_j] = j;
                System.out.println("Population Size = "+i+" | Mutation Rate = "+j);
                population = new ArrayList<>();
                for(int k=0; k<i; k++){
                    population.add(tree_generator.createRecTree(max_depth));
                }
                for(int k=0; k<generations; k++){
                    Tuple selected = tournament();
                    if(reproduction_op.equals("crossover")){
                        crossover(selected);
                    }
                    else{
                        mutation(selected, j);
                    }
                    Double fit = ((Double[]) selected.getThird())[2];
                    map[ind_i][ind_j] = ((Long) Math.round(fit)).doubleValue();
                }
                ind_j++;
            }
            ind_i++;
            ind_j=0;
        }
        population = aux;
        double[][] matrix = DataUtils.unbox(map);
        HeatChart heatmap = new HeatChart(matrix);
        heatmap.setXValues(x_axis);
        heatmap.setYValues(y_axis);
        heatmap.saveToFile(new File("src/tarea3/heatchart.png"));
    }
}
