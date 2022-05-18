package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public abstract class UnaryInstruction extends BaseInstruction {
    protected SymbolVariable parameter;
    protected OperatorTypes operator;

    public UnaryInstruction(SymbolCallable associatedCallable, SymbolVariable result,
                            SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(result));
        this.parameter = parameter;
        this.operator = operator;
    }

    @Override
    public int getNeededStackSize() {
        return 1;
    }
}
