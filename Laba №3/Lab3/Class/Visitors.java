package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.CanSee;
import Lab3.Interface.LooksLike;

public class Visitors extends Person implements CanSee, LooksLike {

    @Override
    public String look() {
        return "*смотрят*";
    }

    @Override
    public String see() {
        return "и видят";
    }

    @Override
    public String sit() {
        return "все дружно уселись";
    }

    @Override
    public String describe() {
        return "обычная толпа людей";
    }
}
