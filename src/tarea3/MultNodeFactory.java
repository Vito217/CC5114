package tarea3;

public class MultNodeFactory implements NodeFactory {
    @Override
    public Node generateNode() {
        return new MultNode(null, null);
    }
}
