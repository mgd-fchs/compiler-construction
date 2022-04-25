package at.tugraz.ist.cc.instructions;

import java.util.List;

public class ConditionalInstruction {
    public final List<Object> ifInstructions;
    public final List<Object> elseInstructions;
    public final Object conditional;

    public ConditionalInstruction(Object conditional, List<Object> ifInstructions, List<Object> elseInstructions) {
        this.ifInstructions = ifInstructions;
        this.elseInstructions = elseInstructions;
        this.conditional = conditional;
    }
}
