package Lab2.Pokemons;

import Lab2.Attacks.SpecialAttacks.FocusBlast;

public class Bisharp extends Pawniard{
    public Bisharp(String name, int lvl){
        super(name, lvl);
        this.setStats(65, 125, 100, 60, 70, 70);
        this.addMove(new FocusBlast());
    }
}
