package Lab3;

import Lab3.Class.Machine;
import Lab3.Class.Shpuntik;
import Lab3.Class.Smekailo;
import Lab3.Class.Visitors;

public class Start {
    public static void main(String[] args){
        Shpuntik shpuntik = new Shpuntik();
        shpuntik.setName("Шпунтик");

        Smekailo smekailo = new Smekailo();
        smekailo.setName("Смекайло");

        Machine machine = new Machine();

        Visitors visitors = new Visitors();
        visitors.setName("Посетители");

        story(shpuntik, smekailo, visitors, machine);
    }

    public static void story(Shpuntik shpuntik, Smekailo smekailo, Visitors visitors, Machine machine){
        System.out.println(smekailo);
        System.out.println(smekailo.show());
        System.out.println(visitors);
        System.out.println(visitors.look() + " " + visitors.see() + " " + machine.describe());
        System.out.println(shpuntik);
        System.out.println("*" + shpuntik.look() + " и " + shpuntik.see() + "*");
        System.out.println(smekailo);
        smekailo.pressButton(machine);
        System.out.println(shpuntik);
        System.out.println("*" + shpuntik.see() + "*");
        System.out.println(shpuntik.sit());
    }

}
