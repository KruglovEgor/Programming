package Lab2.Pokemons;

import Lab2.Attacks.PhysicalAttacks.FeintAttack;
import Lab2.Attacks.PhysicalAttacks.MetalClaw;
import Lab2.Attacks.StatusAttacks.Confide;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Pawniard extends Pokemon {
    public Pawniard (String name, int lvl){
        super(name, lvl);
        this.setType(Type.DARK, Type.STEEL);
        this.setStats(45, 85, 70, 40, 40, 60);
        this.addMove(new FeintAttack());
        this.addMove(new MetalClaw());
        this.addMove(new Confide());
    }
}
