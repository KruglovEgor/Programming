package Lab4.Class;

import Lab4.Abstract.Person;
import Lab4.Interface.CanProduceSounds;
import Lab4.Interface.CanTalk;

public class Someone extends Person implements CanProduceSounds, CanTalk {
    @Override
    public String sit() {
        return "сел";
    }

    @Override
    public String standUp() {
        return "спокойно встал из-за стола";
    }

    @Override
    public String laugh() {
        return "Ха-ха-ха";
    }

    @Override
    public String say(String txt) {
        return "сказал: " + '"' + txt + '"';
    }

    @Override
    public String makeNoise() {
        return "зашумел";
    }

    @Override
    public String parodyCock() {
        return "Кукареку кукареку";
    }

    @Override
    public String meow() {
        return "Мяу-мяу-мяу";
    }

    @Override
    public String parodySheep() {
        return "Бееееее бееееее беееееееееееееееееее";
    }

    @Override
    public String parodyDonkey() {
        return "И-о и-о и-о";
    }

    @Override
    public String parodyHorse() {
        return "И-го-го-го";
    }

    @Override
    public String parodyDog() {
        return "Гав-гав-гав";
    }
}
