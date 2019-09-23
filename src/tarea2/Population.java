package tarea2;

public interface Population {

    void initPopulation(int ps, int ng);

    Object[][] tournament(double selection_rate, FitFun fit);

    Object[][] crossover(double mutation_rate, Object[][] selected);

    Object[][] getPopulation();

    void printPopulation();
}
