package Lab3.Abstract;

public abstract class Person {
    protected String name;

    public abstract String sit();
    public void setName(String name){
        this.name = name;
    }
            ;
    public String getName(){
        return this.name;
    }

    public String toString(){
        return this.getName() + ":";
    }

}
