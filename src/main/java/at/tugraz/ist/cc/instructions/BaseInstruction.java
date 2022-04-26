package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public abstract class BaseInstruction {
    private SymbolVariable result;

    public abstract String buildAssembly();
}
