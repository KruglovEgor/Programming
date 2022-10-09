package Lab2;

import Lab2.Pokemons.*;
import ru.ifmo.se.pokemon.Battle;


public class Start {
    public static void main (String[] args){
        //Min lvls: Tapu Lele - 1; Zweilous - 55; Bisharp - 25; Deino - 52; Pawniward - 25; Hydregion - 55.
        //1 team
        //2 attacks may be learned any time, 2 can't be learned at all(
        TapuLele tapuLele = new TapuLele("Fly", 1);
        Zweilous zweilous = new Zweilous("Mid dragon", 55);
        Bisharp bisharp = new Bisharp("Shrewder", 25);

        //2 team
        Deino deino = new Deino("Mini dragon", 52);
        Pawniard pawniard = new Pawniard("Srewdy", 25);
        Hydreigon hydreigon = new Hydreigon("Big dragon", 55);

        Battle b = new Battle();
        b.addAlly(tapuLele); b.addAlly(zweilous); b.addAlly(bisharp);
        b.addFoe(deino); b.addFoe(pawniard); b.addFoe(hydreigon);
        b.go();
    }
}
