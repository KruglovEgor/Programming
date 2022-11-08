package Lab4.Abstract;

import Lab4.Exceptions.TooShortNameException;

public abstract class Person {
    private String name;

    public abstract String sit();
    public abstract String standUp();
    public void setName(String name) throws TooShortNameException {
        if (name.length() < 2){
            throw new TooShortNameException("Что-то слишком короткое имя Вы ввели!");
        }
        else this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public String toString(){
        return this.getName() + ":";
    }

}
