package tarea3;

public class AddNode extends BinaryNode{

    public AddNode(int arg_num, Node left, Node right) {
        super((a, b) -> { return (int) a + (int) b; }, arg_num, left, right);
    }
}
