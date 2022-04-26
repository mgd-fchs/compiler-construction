package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;

import java.util.List;
import java.util.Optional;

public class ConditionalInstruction extends BaseInstruction{
    public final List<BaseInstruction> ifInstructions;
    public final List<BaseInstruction> elseInstructions;
    public final Object conditional; // TODO change to base instruction

    public ConditionalInstruction(SimpleCallable associatedCallable, Object conditional, List<BaseInstruction> ifInstructions, List<BaseInstruction> elseInstructions) {
        super(associatedCallable, Optional.empty());
        this.ifInstructions = ifInstructions;
        this.elseInstructions = elseInstructions;
        this.conditional = conditional;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
