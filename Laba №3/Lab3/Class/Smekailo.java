package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.forSmekailo;

public class Smekailo extends Person implements forSmekailo {
    protected String name = "Смекайло";

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName()+":";
    }

    @Override
    public String show() {
        return "Вот, смотрите!";
    }

    @Override
    public void pressButton(Machine machine) {
        System.out.println("А теперь, я нажму на кнопку.");
        machine.transform();
    }
}
