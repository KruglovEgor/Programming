package Lab4.Abstract;

import Lab4.Exceptions.DidntActivateException;
import Lab4.Interface.LooksLike;
import Lab4.Enum.Conditions;

public abstract class Creation implements LooksLike {

    private Conditions condition = Conditions.OFF;

    protected class Button{
        public void pushButton() throws DidntActivateException {
            if (Math.random() >= 0.5){
                condition = Conditions.ON;
            }
            else
                throw new DidntActivateException("Машина не запустилась\n Видимо сегодня посетители не увидят представление :-(");
        }
    }

    protected Button button = new Button();

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
