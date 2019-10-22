package tarea3;

public class SubNode extends BinaryNode{

    public SubNode(Node left, Node right) {
        super(args -> {
            try{
                return (int) args[0] - (int) args[1];
            } catch (ClassCastException e) {
                return "("+ args[0] + "-" + args[1]+")";
            }
        },
        left,
        right);
    }

    @Override
    public Node copy() {
        return new SubNode(arguments.get(0), arguments.get(1));
    }

    @Override
    public void print() {
        System.out.print("(");
        arguments.get(0).print();
        System.out.print("-");
        arguments.get(1).print();
        System.out.print(")");
    }
}
