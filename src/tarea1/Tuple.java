package tarea1;

public class Tuple {

    private Object first;
    private Object second;
    private Object third;

    public Tuple(Object o1, Object o2){
        first = o1;
        second = o2;
        third = null;
    }

    public Tuple(Object o1, Object o2, Object o3){
        first = o1;
        second = o2;
        third = o3;
    }

    public Object getFirst(){
        return first;
    }

    public Object getSecond(){
        return second;
    }

    public Object getThird() {
        return third;
    }
}
