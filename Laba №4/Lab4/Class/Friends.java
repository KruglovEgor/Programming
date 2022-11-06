package Lab4.Class;

import Lab4.Abstract.Person;
import Lab4.Interface.CanSee;

public class Friends extends Person implements CanSee {

    @Override
    public String sit() {
        return "уселись за стол";
    }

    @Override
    public String standUp() {
        return "дружно встали из-за стола";
    }


    public String getObject(String object){
        return "получили " + object;
    }

    public String sayGodBye(){
        return "попращались";
    }

    public String goOut(){
        return "вышли на улицу";
    }

    @Override
    public String look() {
        return "посмотрели";
    }

    @Override
    public String see() {
        return "увидели";
    }

    @Override
    public String seeWhat() {
        return "увидели, что ";
    }
}
