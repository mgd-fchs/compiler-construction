package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.List;
import java.util.Optional;

public abstract class ConditionalInstruction extends BaseInstruction{
    public final List<BaseInstruction> ifInstructions;
    public final List<BaseInstruction> conditionals;

    public ConditionalInstruction(SymbolCallable associatedCallable,
                                  List<BaseInstruction> conditionals,
                                  List<BaseInstruction> ifInstructions,
                                  Optional<SymbolVariable> result) {
        super(associatedCallable, result);
        this.ifInstructions = ifInstructions;
        this.conditionals = conditionals;
    }
}
