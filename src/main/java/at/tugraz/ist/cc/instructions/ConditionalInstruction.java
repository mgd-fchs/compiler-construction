package at.tugraz.ist.cc.instructions;

import java.util.List;

public class ConditionalInstruction extends BaseInstruction{
    public final List<BaseInstruction> ifInstructions;
    public final List<BaseInstruction> elseInstructions;
    public final Object conditional;

    public ConditionalInstruction(Object conditional, List<BaseInstruction> ifInstructions, List<BaseInstruction> elseInstructions) {
        this.ifInstructions = ifInstructions;
        this.elseInstructions = elseInstructions;
        this.conditional = conditional;
    }

    @Override
    public String buildAssembly() {
        // TODO
        return null;
    }
}
