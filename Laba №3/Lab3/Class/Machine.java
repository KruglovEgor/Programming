package Lab3.Class;

import Lab3.Interface.LooksLike;

public class Machine implements LooksLike {
    private Conditions condition = Conditions.OFF;

    @Override
    public String describe(){
        if (equals(1)) return "небольшой столик со стульчиком";
        else return "какое-то неуклюжее сооружение, напоминавшее не то сложенную палатку, не то зонтик больших размеров";
    }

    @Override
    public boolean equals(Object obj) {
        Integer i = (Integer) obj;
        return this.hashCode() == i;
    }

    public void transform(){
        condition = Conditions.ON;
        System.out.println("Машина превратилась в " + describe());
    }

    @Override
    public int hashCode() {
        if (condition == Conditions.ON) return 1;
        else return 0;
    }
}
