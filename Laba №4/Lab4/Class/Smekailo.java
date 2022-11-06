package Lab4.Class;

import Lab4.Abstract.Creation;
import Lab4.Abstract.Person;
import Lab4.Interface.CanPress;
import Lab4.Interface.CanShow;
import Lab4.Interface.LooksLike;

public class Smekailo extends Person implements CanShow, CanPress, LooksLike {

    @Override
    public String show() {
        return "Вот, смотрите!";
    }

    @Override
    public void pressButton(Creation creation) {
        System.out.println("*нажал на кнопку*");
        creation.transform();
    }

    @Override
    public String sit() {
        return "сел самым обычным способом";
    }

    @Override
    public String standUp() {
        return "просто встал из-за стола";
    }

    @Override
    public String describe() {
        return "мальчик среднего роста в бордовом костюме";
    }

    public String getAngry(){
        return "насупился";
    }
}
