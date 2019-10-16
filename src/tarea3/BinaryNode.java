package tarea3;

public abstract class BinaryNode extends Node{

    public BinaryNode(Function f, Node left, Node right) {
        super(f);
        addArgument(left);
        addArgument(right);
    }

    public abstract Node copy();

    public abstract void print();
}
