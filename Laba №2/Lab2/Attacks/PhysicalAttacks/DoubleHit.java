package Lab2.Attacks.PhysicalAttacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class DoubleHit extends PhysicalMove {
    public DoubleHit(){
        super(Type.NORMAL, 35, 0.9, 0, 2);
    }

    @Override
    protected String describe(){
        return "использует атаку Double Hit";
    }
}
