package Lab4.Class;

import Lab4.Abstract.Creation;
import Lab4.Enum.Conditions;

public class Machine extends Creation{

    @Override
    public String describe(){
        if (equals(1)) return "небольшой столик со стульчиком";
        else return "какое-то неуклюжее сооружение, напоминавшее не то сложенную палатку, не то зонтик больших размеров";
    }

    public void transform(){
        condition = Conditions.ON;
        System.out.println("*Машина превратилась в " + describe() + "*");
    }

}
