package tarea3;

public class MaxNodeFactory implements NodeFactory{
    @Override
    public Node generateNode() {
        return new MaxNode(null, null);
    }
}
