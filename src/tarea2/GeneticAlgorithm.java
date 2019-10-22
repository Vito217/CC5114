package tarea2;

import javafx.util.Pair;
import org.tc33.jheatchart.HeatChart;
import tarea1.Tuple;
import tarea1.DataUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GeneticAlgorithm{

    private double mutation_rate;
    private int population_size;
    private int number_of_gens;
    private ArrayList<Pair<FitFun, Double>> fitfun;
    private Population population;
    private boolean elitist;
    private boolean pareto;
    private String reproduction;

    public GeneticAlgorithm(double mutation_rate, int population_size,
                            int number_of_gens, String reproduction,
                            boolean elitist, boolean pareto,
                            Population population, ArrayList<Pair<FitFun, Double>> fitfun){
        this.mutation_rate = mutation_rate;
        this.population_size = population_size;
        this.number_of_gens = number_of_gens;
        this.fitfun = fitfun;
        population.initPopulation(population_size, number_of_gens);
        this.population = population;
        this.elitist = elitist;
        this.pareto = pareto;
        this.reproduction = reproduction;
    }

    // SELECTION
    public Tuple tournament() {
        return population.tournament(fitfun, pareto);
    }

    // GENETIC OPERATORS
    public Object[][] crossover(Tuple selected){
        return population.crossover(mutation_rate, selected, elitist);
    }

    public Object[][] mutation(Tuple selected){
        return population.mutation(mutation_rate, selected, elitist);
    }

    public double[][] solve(int iterations){
        Double[][] fit_per_gen = new Double[iterations][fitfun.size()];
        for(int i=0; i<iterations; i++){
            population.printPopulation();
            Tuple selected = tournament();
            fit_per_gen[i] = (Double[]) selected.getThird();
            if(reproduction.equals("crossover")){
                crossover(selected);
            }
            else{
                mutation(selected);
            }
        }

        return DataUtils.transpose(DataUtils.unbox(fit_per_gen));
    }

    public void get_heatmap(int[] population_range, double[] mutation_range,
                                  int population_step, double mutation_step,
                                  String reproduction_op, int generations) throws IOException {

        System.out.println("Computing Heat Map using "+reproduction_op+" and "+generations+" generations");

        Population aux = population;
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
                population.initPopulation(i, number_of_gens);
                for(int k=0; k<generations; k++){
                    Tuple selected = tournament();
                    if(reproduction_op.equals("crossover")){
                        population.crossover(mutation_rate, selected, elitist);
                    }
                    else{
                        population.mutation(mutation_rate, selected, elitist);
                    }
                    Double fit = ((Double[]) selected.getThird())[2];
                    map[ind_i][ind_j] = ((Long) Math.round(fit)).doubleValue();
                }
                ind_j++;
            }
            ind_i++;
            ind_j=0;
        }

        double[][] matrix = DataUtils.unbox(map);
        HeatChart heatmap = new HeatChart(matrix);
        heatmap.setXValues(x_axis);
        heatmap.setYValues(y_axis);
        heatmap.saveToFile(new File("src/tarea2/java-heat-chart.png"));
    }
}
