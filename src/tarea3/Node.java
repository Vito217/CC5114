package tarea3;

import java.util.ArrayList;

public abstract class Node {

    protected Node parent;
    protected Function function;
    protected ArrayList<Node> arguments;

    public Node(Function f){
        function = f;
        arguments = new ArrayList<>();
        parent = null;
    }

    public Node(Function f, ArrayList<Node> args){
        function = f;
        arguments = args;
        parent = null;
    }

    public ArrayList<Node> getArguments(){
        return arguments;
    }

    public Function getFunction(){
        return function;
    }

    public void addArgument(Node n){
        if(n != null){
            n.setParent(this);
        }
        arguments.add(n);
    }

    public void setArguments(ArrayList<Node> args){
        arguments = args;
    }

    public void setChild(Node n, int index) {
        arguments.set(index, n);
    }

    public void setParent(Node p){
        parent = p;
    }

    public Object eval(){
        ArrayList<Object> args = new ArrayList<>();
        for(Node node: arguments){
            args.add(node.eval());
        }
        Object[] arglist = args.toArray();
        return function.function(arglist);
    }

    public ArrayList<Node> serialize(){
        ArrayList<Node> serial = new ArrayList<>();
        serial.add(this);
        for(Node node: arguments){
            ArrayList<Node> child_serial = node.serialize();
            serial.addAll(child_serial);
        }
        return serial;
    }

    public void replace(Node otherNode){
        if(parent != null){
            ArrayList<Node> parent_arguments = parent.getArguments();
            int index = parent_arguments.indexOf(this);
            parent.setChild(otherNode, index);
            otherNode.setParent(parent);
        }
    }

    public abstract Node copy();

    public abstract void print();

}
