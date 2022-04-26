package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class BinaryInstruction extends BaseInstruction {
    private SymbolVariable leftParam;
    private SymbolVariable rightParam;
    private OperatorTypes operator;

    public BinaryInstruction(SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }

    @Override
    public String buildAssembly() {
        // TODO
        return null;
    }
}
