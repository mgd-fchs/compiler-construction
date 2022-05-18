package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class AssignLocalInstruction extends BaseInstruction {
    private SymbolVariable lhs;
    private SymbolVariable rhs;

    public AssignLocalInstruction(SymbolCallable associatedCallable, SymbolVariable lhs, SymbolVariable rhs) {
        super(associatedCallable, Optional.empty());
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(rhs))
                .append(popVariableFromStack(lhs))
                .append("\n\n");

        return builder.toString();
    }

    @Override
    public int getNeededStackSize() {
        return 1;
    }
}
