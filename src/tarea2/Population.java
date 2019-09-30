package tarea2;

import javafx.util.Pair;
import tarea1.Tuple;
import java.util.ArrayList;

public interface Population {

    void initPopulation(int ps, int ng);

    Tuple tournament(double selection_rate, ArrayList<Pair<FitFun, Double>> fitfun, boolean pareto);

    Object[][] crossover(double mutation_rate, Tuple selected, boolean elitist);

    Object[][] getPopulation();

    void printPopulation();
}
