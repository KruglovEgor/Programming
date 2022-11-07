package Lab4.Class;

import Lab4.Abstract.Creation;
import Lab4.Interface.LooksLike;


public class Case extends Creation {

    public String spin(){
        class Disk implements LooksLike {
            public String describe(){
                return "какой-то диск, имевшийся под крышкой чемодана";
            }
        }
        Disk disk = new Disk();
        return "повертел " + disk.describe();
    }

    public void transform(){
        button.pushButton();
        makeNoise();
    }

    public void makeNoise(){
        if (equals(1))  System.out.println("*Послышалось шипение, раздался удар, словно захлопнулась дверь.*");
        else System.out.println("*ничего не cлышно*");
    }

    @Override
    public String describe() {
        return "чемодан";
    }
}
