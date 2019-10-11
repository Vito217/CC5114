package tarea2;

public class PopulationFactory {

    public Population createStringPopulation(){
        return new StringPopulation();
    }

    public Population createBinaryPopulation(){
        return new BinaryPopulation();
    }

    public Population createUBKPopulation(){
        return new UBKPopulation();
    }
}
