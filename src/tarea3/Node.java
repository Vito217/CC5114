package tarea3;

import java.util.ArrayList;

public class Node {

    protected Function function;
    protected int number_of_arguments;
    protected Node[] arguments;
    protected int argumets_pointer;

    public Node(Function f, int arg_num){
        function = f;
        number_of_arguments = arg_num;
        arguments = new Node[number_of_arguments];
        argumets_pointer = 0;
    }

    public void eval(){
        for(Node node: arguments){
            node.eval();
        }
    }

    public ArrayList<Node> serialize(){
        ArrayList<Node> serial = new ArrayList<>();
        for(Node node: arguments){
            ArrayList<Node> child_serial = node.serialize();
            serial.addAll(child_serial);
        }
        return serial;
    }

    public Node copy(){
        return new Node(function, number_of_arguments);
    }

    public void replace(Node otherNode){

    }

}
