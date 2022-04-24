package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class PrintInstruction {
    SymbolVariable printValue;

    public PrintInstruction(SymbolVariable printValue) {
        this.printValue = printValue;
    }
}
