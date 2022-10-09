package Lab2.Pokemons;

import Lab2.Attacks.StatusAttacks.ScaryFace;
import Lab2.Attacks.StatusAttacks.Swagger;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Deino extends Pokemon {
    public Deino(String name, int lvl){
        super(name, lvl);
        this.setType(Type.DARK, Type.DRAGON);
        this.setStats(52, 65, 50, 45, 50, 38);
        this.addMove(new ScaryFace());
        this.addMove(new Swagger());
    }
}
