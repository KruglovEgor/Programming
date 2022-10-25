package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.forShpuntik;

public class Shpuntik extends Person implements forShpuntik {

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
    public String twistLegs() {
        return "*скрутил ноги самым неестественным способом*";
    }

    @Override
    public String sit() {
        return "*сел за стол*";
    }

}
