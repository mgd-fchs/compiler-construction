package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class AssignInstruction {
    private SymbolVariable paramName;
    private SymbolVariable value;
    private OperatorTypes operator;

    public AssignInstruction(SymbolVariable paramName, SymbolVariable value) {
        this.paramName = paramName;
        this.value = value;
    }
}
