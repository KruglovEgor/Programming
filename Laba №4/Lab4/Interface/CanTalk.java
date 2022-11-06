package Lab4.Interface;

public interface CanTalk {
    String say(String txt);

    default String ask(String txt){
        return "спросил " + txt;
    }


}
