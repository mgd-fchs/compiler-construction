package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class ReturnInstruction extends BaseInstruction {
    private SymbolVariable returnValue;

    public ReturnInstruction(SimpleCallable associatedCallable, SymbolVariable returnValue) {
        super(associatedCallable, Optional.of(returnValue));
        this.returnValue = returnValue;
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
