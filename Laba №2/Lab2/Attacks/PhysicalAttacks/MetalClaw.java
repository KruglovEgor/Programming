package Lab2.Attacks.PhysicalAttacks;

import ru.ifmo.se.pokemon.*;

public class MetalClaw extends PhysicalMove {
    public MetalClaw(){
        super(Type.STEEL, 50, 0.95);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        Effect effect = new Effect().chance(0.1);
        if (effect.success()){
            pokemon.setMod(Stat.ATTACK, 1);
        }
    }

    @Override
    protected String describe() {
        return "использует атаку Metal Claw";
    }
}
