package Lab2.Attacks.PhysicalAttacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class FeintAttack extends PhysicalMove {
    public FeintAttack(){
        super(Type.DARK, 60, 1);
    }

    @Override
    protected String describe() {
        return "использует атаку Feint Attack";
    }
}
