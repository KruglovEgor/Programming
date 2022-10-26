package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.CanSee;
import Lab3.Interface.LooksLike;

public class Shpuntik extends Person implements CanSee, LooksLike {

    @Override
    public String sit() {
        return "*сел за стол, скрутив ноги самым неестественным способом*";
    }

    @Override
    public String look() {
        return "тоже смотрел";
    }

    @Override
    public String see() {
        return "увидел это";
    }

    @Override
    public String describe() {
        return "маленький весёлый мальчик";
    }
}
