package at.tugraz.ist.cc.symbol_table;

public class BinaryInstruction {
    private SymbolVariable leftParam;
    private SymbolVariable rightParam;
    private OperatorTypes operator;

    public BinaryInstruction(SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }
}
