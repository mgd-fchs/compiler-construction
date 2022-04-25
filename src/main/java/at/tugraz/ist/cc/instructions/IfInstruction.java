package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class IfInstruction {
    private Integer currentLabelIndex;
    private Boolean elseBlock;

    public IfInstruction(Integer currentLabelIndex, Boolean elseBlock) {
        this.currentLabelIndex = currentLabelIndex;
        this.elseBlock = elseBlock;
    }
}
