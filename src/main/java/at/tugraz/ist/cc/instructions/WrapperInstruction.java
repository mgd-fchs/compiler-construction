package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class WrapperInstruction extends BaseInstruction{

    public WrapperInstruction(SimpleCallable associatedCallable, SymbolVariable resultToWrap) {
        super(associatedCallable, Optional.empty());
        this.result = resultToWrap;
    }

    @Override
    public String buildAssemblyString() {
        return "";
    }
}
