package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public abstract class BinaryInstruction extends BaseInstruction {
    protected SymbolVariable leftParam;
    protected SymbolVariable rightParam;
    protected OperatorTypes operator;

    public BinaryInstruction(SimpleCallable associatedCallable, SymbolVariable result,
                             SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(result));
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }
}
