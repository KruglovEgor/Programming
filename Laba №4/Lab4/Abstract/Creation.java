package Lab4.Abstract;

import Lab4.Interface.LooksLike;
import Lab4.Enum.Conditions;

public abstract class Creation implements LooksLike {
    protected Conditions condition = Conditions.OFF;

    @Override
    public int hashCode() {
        if (condition == Conditions.ON) return 1;
        else return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Integer i = (Integer) obj;
        return this.hashCode() == i;
    }

    public abstract void transform();
}
