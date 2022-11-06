package Lab4.Class;

import Lab4.Abstract.Person;
import Lab4.Interface.CanSee;
import Lab4.Interface.LooksLike;

public class Visitors extends Person implements CanSee, LooksLike {

    @Override
    public String look() {
        return "смотрят";
    }

    @Override
    public String see() {
        return "и видят";
    }

    @Override
    public String seeWhat() {
        return "увидели, что";
    }

    @Override
    public String sit() {
        return "все дружно уселись";
    }

    @Override
    public String standUp() {
        return "встали из-за стола";
    }

    @Override
    public String describe() { return "обычная толпа людей";}
}
