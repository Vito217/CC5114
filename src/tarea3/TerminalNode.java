package tarea3;

public class TerminalNode extends Node{

    private Object value;

    public TerminalNode(Object val) {
        super(null);
        value = val;
    }

    public Object eval() {
        return value;
    }

    @Override
    public Node copy() {
        return new TerminalNode(value);
    }

    @Override
    public void print() {
        System.out.print(value);
    }
}
