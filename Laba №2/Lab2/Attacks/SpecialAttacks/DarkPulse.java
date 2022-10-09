package Lab2.Attacks.SpecialAttacks;

import ru.ifmo.se.pokemon.*;

public class DarkPulse extends SpecialMove {
    public DarkPulse(){
        super(Type.DARK, 80, 1);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect().chance(0.2);
        if (effect.success()){
            Effect.flinch(pokemon);
        }
    }

    @Override
    protected String describe(){
        return "использует специальную атаку Dark Pulse";
    }
}
