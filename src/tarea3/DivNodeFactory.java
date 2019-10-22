package tarea3;

public class DivNodeFactory implements NodeFactory{
    @Override
    public Node generateNode() {
        return new DivNode(null, null);
    }
}
