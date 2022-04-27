package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public abstract class BinaryInstruction extends BaseInstruction {
    protected SymbolVariable leftParam;
    protected SymbolVariable rightParam;
    protected OperatorTypes operator;

    public BinaryInstruction(SimpleCallable associatedCallable, SymbolVariable result,
                             SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(result)); // TODO automatic casting ??
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }
}
