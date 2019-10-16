package tarea3;

import java.util.ArrayList;
import java.util.Random;

public class AST {

    private Random rand = new Random();
    private ArrayList<NodeFactory> functions;
    private ArrayList<Object> terminals;
    private float prob;

    public AST(ArrayList<NodeFactory> allowed_functions, ArrayList<Object> allowed_terminals, float prob_terminal){
        terminals = allowed_terminals;
        functions = allowed_functions;
        prob = prob_terminal;
    }

    public Node createRecTree(int depth){
        if(depth > 0){
            Node node_cls = functions.get(rand.nextInt(functions.size())).generateNode();
            ArrayList<Node> arguments = new ArrayList<>();
            for(int i=0; i<node_cls.getArguments().size(); i++){
                if(rand.nextFloat() < prob){
                    arguments.add(createRecTree(0));
                }
                else {
                    arguments.add(createRecTree(depth-1));
                }
            }
            node_cls.setArguments(arguments);
            return node_cls;
        }
        else {
            return new TerminalNode(terminals.get(rand.nextInt(terminals.size())));
        }
    }

}
