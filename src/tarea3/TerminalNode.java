package tarea3;

import java.util.HashMap;

public class TerminalNode extends Node{

    private Object value;

    public TerminalNode(Object val) {
        super(null);
        value = val;
    }

    public Object eval() {
        return value;
    }

    public Object eval(HashMap<Object, Integer> vars) {
        try{
            int val = (int) value;
            return val;
        }
        catch (ClassCastException e){
            int val = vars.get(value);
            return val;
        }
    }

    @Override
    public Node copy() {
        return new TerminalNode(value);
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public int depth(){
        return 0;
    }

    @Override
    public void repetitions(HashMap<Object, Integer> reps){
        if(reps.containsKey(value)){
            reps.put(value, reps.get(value) + 1);
        }
        else{
            reps.put(value, 0);
        }
    }
}
