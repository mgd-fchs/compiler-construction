package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

import static at.tugraz.ist.cc.CompatibilityCheckUtils.TYPE_PRIMITIVE;

public class BinaryInstruction extends BaseInstruction {
    private SymbolVariable leftParam;
    private SymbolVariable rightParam;
    private OperatorTypes operator;

    public BinaryInstruction(SimpleCallable associatedCallable, SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT))); // TODO automatic casting ??
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
