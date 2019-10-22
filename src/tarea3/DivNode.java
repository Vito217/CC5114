package tarea3;

public class DivNode extends BinaryNode{

    public DivNode(Node left, Node right){
        super(args -> {
            try{
                return (int) args[0] / (int) args[1];
            } catch (ArithmeticException e) {
                return "undefined";
            }
        },
        left,
        right);
    }

    @Override
    public Node copy() {
        return new DivNode(arguments.get(0), arguments.get(1));
    }

    @Override
    public void print() {
        System.out.print("(");
        arguments.get(0).print();
        System.out.print("/");
        arguments.get(1).print();
        System.out.print(")");
    }
}
