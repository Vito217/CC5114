package tarea3;

public class AddNodeFactory implements NodeFactory{
    @Override
    public Node generateNode() {
        return new AddNode(null, null);
    }
}
