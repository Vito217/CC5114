package tarea3;

public class MultNode extends BinaryNode {

    public MultNode(Node left, Node right) {
        super((args) -> (int) args[0] * (int) args[1], left, right);
    }

    @Override
    public Node copy() {
        return new MultNode(arguments.get(0), arguments.get(1));
    }

    @Override
    public void print() {
        System.out.print("(");
        arguments.get(0).print();
        System.out.print("*");
        arguments.get(1).print();
        System.out.print(")");
    }
}
