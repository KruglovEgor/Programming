package Lab3.Interface;
import Lab3.Class.Machine;

@FunctionalInterface
public interface CanPress {
    void pressButton(Machine machine);
}
