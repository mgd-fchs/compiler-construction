package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;

import java.util.List;
import java.util.Optional;

public class ConditionalInstruction extends BaseInstruction{
    public final List<BaseInstruction> ifInstructions;
    public final List<BaseInstruction> elseInstructions;
    public final List<BaseInstruction> conditionals;

    public ConditionalInstruction(SimpleCallable associatedCallable,
                                  List<BaseInstruction> conditionals, List<BaseInstruction> ifInstructions,
                                  List<BaseInstruction> elseInstructions) {
        super(associatedCallable, Optional.empty());
        this.ifInstructions = ifInstructions;
        this.elseInstructions = elseInstructions;
        this.conditionals = conditionals;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
