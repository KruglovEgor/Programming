package Lab4.Abstract;

public abstract class Person {
    protected String name;

    public abstract String sit();
    public abstract String standUp();
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public String toString(){
        return this.getName() + ":";
    }

}
