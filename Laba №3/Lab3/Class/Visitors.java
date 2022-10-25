package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.forVisitors;

public class Visitors extends Person implements forVisitors {
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
    public String look() {
        return "*смотрят*";
    }

    @Override
    public String see() {
        return "и видят";
    }
}
