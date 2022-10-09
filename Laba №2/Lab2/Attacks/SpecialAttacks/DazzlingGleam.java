package Lab2.Attacks.SpecialAttacks;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class DazzlingGleam extends SpecialMove {

    public DazzlingGleam(){
        super(Type.FAIRY, 80, 1);
    }

    @Override
    protected String describe(){
        return "использует атаку Dazzling Gleam";
    }
}
