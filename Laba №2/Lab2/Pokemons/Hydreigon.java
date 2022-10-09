package Lab2.Pokemons;

import Lab2.Attacks.SpecialAttacks.DarkPulse;

public class Hydreigon extends Zweilous{
    public Hydreigon(String name, int lvl){
        super(name, lvl);
        this.setStats(92, 105, 90, 125, 90, 98);
        this.addMove(new DarkPulse());
    }
}
