package tarea2;

import javafx.util.Pair;
import tarea1.Tuple;
import java.util.ArrayList;
import java.util.Random;

public abstract class Population {

    protected Random rand = new Random();

    public Population(){

    }

    abstract void initPopulation(int ps, int ng);

    abstract Tuple tournament(ArrayList<Pair<FitFun, Double>> fitfun, boolean pareto);

    abstract Object[][] crossover(double mutation_rate, Tuple selected, boolean elitist);

    abstract Object[][] mutation(double mutation_rate, Tuple selected, boolean elitist);

    abstract Object[][] getPopulation();

    abstract void printPopulation();

}
