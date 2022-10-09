package Lab2.Attacks.PhysicalAttacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class BraveBird extends PhysicalMove {

    public BraveBird(){
        super(Type.FLYING, 120, 1);
    }

    @Override
    protected void applySelfDamage(Pokemon pokemon, double v) {
        //надо реализовать получение 1/3 от нанесённого дамага
    }

    @Override
    protected String describe(){
        return "использует атаку Brave Bird";
    }
}
