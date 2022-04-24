package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class LabelInstruction {
    private Integer labelIndex;

    public LabelInstruction(Integer labelIndex) {
        this.labelIndex = labelIndex;
    }
}
