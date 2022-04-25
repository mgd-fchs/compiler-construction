package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class ReturnInstruction {
    private SymbolVariable returnValue;

    public ReturnInstruction(SymbolVariable returnValue) {
        this.returnValue = returnValue;
    }
}
