package Lab2.Attacks.StatusAttacks;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class Roost extends StatusMove {
    public Roost(){
        super(Type.FLYING, 0, 1);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        int halfHP = (int) pokemon.getStat(Stat.HP) / 2;
        pokemon.setMod(Stat.HP, -halfHP);
    }

    @Override
    protected String describe(){
        return "использует атаку Roost";
    }
}
