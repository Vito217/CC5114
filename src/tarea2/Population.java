package tarea2;

import tarea1.Tuple;

public interface Population {

    void initPopulation(int ps, int ng);

    Tuple tournament(double selection_rate, FitFun fit);

    Object[][] crossover(double mutation_rate, Tuple selected, boolean elitist);

    Object[][] getPopulation();

    void printPopulation();
}
