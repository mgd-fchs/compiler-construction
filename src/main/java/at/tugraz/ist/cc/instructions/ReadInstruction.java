package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class ReadInstruction {

    SymbolVariable readValue;

    public ReadInstruction(SymbolVariable readValue) {
        this.readValue = readValue;
    }
}
