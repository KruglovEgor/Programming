package Lab4.Class;

public class SolderingIron {

    private static final String name = "паяльник";
    private static final String material = "железный";

    private final int size_x; private final int size_y; private final int size_z;
    private String colour;

    public SolderingIron(int size_x, int size_y, int size_z, String colour){
        this.size_x = size_x;
        this.size_y = size_y;
        this.size_z = size_z;
        this.colour = colour;
    }

    public void changeColour(String new_colour){
        colour = new_colour;
    }

    public String getSizes(){
        return this.size_x + "x" + size_y + "x" + size_z;
    }

    public static class getSpecification{
        public static String getName(){
            return name;
        }

        public static String getMaterial(){
            return material;
        }
    }


}
