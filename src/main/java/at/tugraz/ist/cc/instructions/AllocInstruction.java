package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class AllocInstruction {
    private SymbolVariable actualType;

    public AllocInstruction(SymbolVariable classType) {
        this.actualType = classType;
    }
}
