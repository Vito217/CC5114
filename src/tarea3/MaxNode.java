package tarea3;

public class MaxNode extends BinaryNode{

    public MaxNode(Node left, Node right) {
        super((args) -> Math.max((int) args[0], (int) args[1]), left, right);
    }

    @Override
    public Node copy() {
        return new MaxNode(arguments.get(0), arguments.get(1));
    }

    @Override
    public void print() {
        System.out.print("Max(");
        arguments.get(0).print();
        System.out.print(",");
        arguments.get(1).print();
        System.out.print(")");
    }
}
