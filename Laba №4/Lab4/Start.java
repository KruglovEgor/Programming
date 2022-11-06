package Lab4;

import Lab4.Class.*;
import Lab4.Interface.SunMoving;

public class Start {
    public static void main(String[] args){

        //TODO make static and non-static inner classes
        //Already have anonyme class - here SunMoving and local class Disk in Case

        Shpuntik shpuntik = new Shpuntik();
        shpuntik.setName("Шпунтик");

        Smekailo smekailo = new Smekailo();
        smekailo.setName("Смекайло");

        Machine machine = new Machine();

        Visitors visitors = new Visitors();
        visitors.setName("Посетители");

        Someone someone = new Someone();
        someone.setName("Кто-то");

        Friends friends = new Friends();
        friends.setName("Друзья");

        Case case_ = new Case();
        Situation situation = new Situation();

        SunMoving sun = new SunMoving() {
            @Override
            public String goUp() {
                return "Солнце восходит";
            }

            @Override
            public String goDown() {
                return "Солнце заходит";
            }
        };

        story(shpuntik, smekailo, visitors, machine, someone, friends, case_, situation, sun);
    }

    private static void story(Shpuntik shpuntik, Smekailo smekailo, Visitors visitors, Machine machine, Someone someone, Friends friends, Case case_, Situation situation, SunMoving sun){
        System.out.println(smekailo);
        System.out.println("*" + case_.spin() + "*");
        smekailo.pressButton(case_);
        System.out.println("*" + situation.getSilent() + "*");
        System.out.println(" ");

        System.out.println(someone.getName() + " " + someone.say("Под столом"));
        System.out.println("*" + situation.getLoud() + "*");
        System.out.println(someone);
        System.out.println(someone.laugh());
        System.out.println(someone);
        System.out.println(someone.parodyCock());
        System.out.println(someone);
        System.out.println(someone.meow());
        System.out.println(someone);
        System.out.println(someone.parodyDog());
        System.out.println(someone);
        System.out.println(someone.parodySheep());
        System.out.println(" ");

        System.out.println(someone.getName() + " " + someone.say("Пустите меня, я покричу ослом"));
        System.out.println(someone);
        System.out.println(someone.parodyDonkey());
        System.out.println(someone);
        System.out.println(someone.parodyHorse());
        System.out.println(someone);
        System.out.println(someone.laugh());
        System.out.println(" ");


        System.out.println(smekailo);
        System.out.println("*" + smekailo.getAngry() + "*");
        System.out.println(smekailo.show());
        System.out.println(visitors);
        System.out.println("*" + visitors.look() + " " + visitors.see() + " " + machine.describe() + "*");
        System.out.println(shpuntik);
        System.out.println("*" + shpuntik.look() + " и " + shpuntik.see() + "*");
        System.out.println(smekailo);
        smekailo.pressButton(machine);
        System.out.println(shpuntik);
        System.out.println("*" + shpuntik.see() + "*");
        System.out.println("*" + shpuntik.sit() + "*");
        System.out.println("*" + shpuntik.feelPain() + "*");
        System.out.println("*" + shpuntik.standUp() + "*");
        System.out.println(" ");

        System.out.println(friends);
        System.out.println("*" + friends.seeWhat() + sun.goDown() + "*");
        System.out.println("*" + friends.getObject("паяльник") + "*");
        System.out.println("*" + friends.sayGodBye() + "*");
        System.out.println("*" + friends.goOut() + "*");
    }

}
