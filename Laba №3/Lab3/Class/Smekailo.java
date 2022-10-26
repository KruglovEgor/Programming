package Lab3.Class;

import Lab3.Abstract.Person;
import Lab3.Interface.CanPress;
import Lab3.Interface.CanShow;
import Lab3.Interface.LooksLike;

public class Smekailo extends Person implements CanShow, CanPress, LooksLike {

    @Override
    public String show() {
        return "Вот, смотрите!";
    }

    @Override
    public void pressButton(Machine machine) {
        System.out.println("А теперь, я нажму на кнопку");
        machine.transform();
    }

    @Override
    public String sit() {
        return "сел самым обычным способом";
    }

    @Override
    public String describe() {
        return "мальчик среднего роста в бордовом костюме";
    }
}
