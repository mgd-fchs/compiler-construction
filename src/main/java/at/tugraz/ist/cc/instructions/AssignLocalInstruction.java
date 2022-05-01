package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class AssignLocalInstruction extends BaseInstruction {
    private SymbolVariable paramName;
    private SymbolVariable value;

    public AssignLocalInstruction(SimpleCallable associatedCallable, SymbolVariable paramName, SymbolVariable value) {
        super(associatedCallable, Optional.empty());
        this.paramName = paramName;
        this.value = value;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(value))
                .append(popVariableFromStack(paramName))
                .append("\n\n");

        return builder.toString();
    }
}
