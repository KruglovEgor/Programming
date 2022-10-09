package Lab2.Pokemons;

import Lab2.Attacks.PhysicalAttacks.BraveBird;
import Lab2.Attacks.SpecialAttacks.DazzlingGleam;
import Lab2.Attacks.StatusAttacks.Confide;
import Lab2.Attacks.StatusAttacks.Roost;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class TapuLele extends Pokemon {
    public TapuLele(String name, int lvl){
        super(name, lvl);
        this.setType(Type.PSYCHIC, Type.FAIRY);
        this.setStats(70, 85, 75, 130, 115, 95);
        this.addMove(new Confide());
        this.addMove(new DazzlingGleam());
        this.addMove(new BraveBird());
        this.addMove(new Roost());
    }
}
