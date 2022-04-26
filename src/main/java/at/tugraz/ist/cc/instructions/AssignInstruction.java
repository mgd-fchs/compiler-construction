package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class AssignInstruction extends BaseInstruction {
    private SymbolVariable paramName;
    private SymbolVariable value;

    public AssignInstruction(SymbolVariable paramName, SymbolVariable value) {
        this.paramName = paramName;
        this.value = value;
    }

    @Override
    public String buildAssembly() {
        // TODO
        return null;
    }
}
