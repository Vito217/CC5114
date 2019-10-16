package tarea3;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {

        ArrayList<NodeFactory> allowed_functions = new ArrayList<NodeFactory>(){
            {
                add(new AddNodeFactory());
                add(new SubNodeFactory());
                add(new MultNodeFactory());
                add(new MaxNodeFactory());
            }
        };

        ArrayList<Object> allowed_terminals = new ArrayList<Object>(){
            {
                add(1);
                add(4);
                add(8);
                add(12);
                add(5);
            }
        };

        AST tree_generator = new AST(allowed_functions, allowed_terminals, 0.5f);
        Node tree = tree_generator.createRecTree(2);
        tree.print();
        System.out.print("="+tree.eval());
    }

}
