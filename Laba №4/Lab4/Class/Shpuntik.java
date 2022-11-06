package Lab4.Class;

import Lab4.Abstract.Person;
import Lab4.Interface.CanSee;
import Lab4.Interface.CanTalk;
import Lab4.Interface.FeelPain;
import Lab4.Interface.LooksLike;

public class Shpuntik extends Person implements CanSee, LooksLike, FeelPain, CanTalk {

    @Override
    public String sit() {
        return "сел за стол, скрутив ноги самым неестественным способом";
    }

    @Override
    public String standUp() {
        return "побыстрее вылез из-за стола и перевёл тему разговора";
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
    public String seeWhat() {
        return "увидел, что";
    }

    @Override
    public String describe() {
        return "маленький весёлый мальчик";
    }

    @Override
    public String feelPain() {
        return "почувствовал зверскую боль";
    }

    @Override
    public String say(String txt) {
        return "сказал " + txt;
    }
}
