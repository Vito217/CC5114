package tarea3;

public class SubNodeFactory implements NodeFactory{
    @Override
    public Node generateNode() {
        return new SubNode(null, null);
    }
}
