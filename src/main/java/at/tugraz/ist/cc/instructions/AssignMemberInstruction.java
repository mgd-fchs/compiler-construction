package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class AssignMemberInstruction extends BaseInstruction {
    private SymbolVariable paramName;
    private SymbolVariable value;
    private SymbolVariable invocationRef;

    public AssignMemberInstruction(SimpleCallable associatedCallable, SymbolVariable invocationRef, SymbolVariable paramName, SymbolVariable value) {
        super(associatedCallable, Optional.empty());
        this.paramName = paramName;
        this.value = value;
        this.invocationRef = invocationRef;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
