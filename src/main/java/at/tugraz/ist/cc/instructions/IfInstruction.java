package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.List;

public class IfInstruction {
    public final List<Object> ifInstructions;
    public final List<Object> elseInstructions;
    public final Object conditional;

    public IfInstruction(Object conditional, List<Object> ifInstructions, List<Object> elseInstructions) {
        this.ifInstructions = ifInstructions;
        this.elseInstructions = elseInstructions;
        this.conditional = conditional;
    }
}
