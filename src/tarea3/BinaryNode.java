package tarea3;

public class BinaryNode extends Node{

    public BinaryNode(Function f, int arg_num, Node left, Node right) {
        super(f, arg_num);
        arguments[argumets_pointer++] = left;
        arguments[argumets_pointer++] = right;
    }
}
