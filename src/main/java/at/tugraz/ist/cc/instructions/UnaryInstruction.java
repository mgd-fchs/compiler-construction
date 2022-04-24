package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class UnaryInstruction {
    private SymbolVariable parameter;
    private OperatorTypes operator;

    public UnaryInstruction(SymbolVariable parameter, OperatorTypes operator) {
        this.parameter = parameter;
        this.operator = operator;
    }
}
