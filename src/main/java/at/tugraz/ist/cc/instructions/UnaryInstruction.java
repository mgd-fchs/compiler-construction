package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class UnaryInstruction extends BaseInstruction {
    private SymbolVariable parameter;
    private OperatorTypes operator;

    public UnaryInstruction(SimpleCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(new SymbolVariable(SymbolType.PRIMITIVE,
                (operator.equals(OperatorTypes.NOT)) ? SymbolPrimitiveType.INT : SymbolPrimitiveType.BOOL)));
        this.parameter = parameter;
        this.operator = operator;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
