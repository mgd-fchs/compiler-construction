package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WrapperInstruction extends BaseInstruction{

    public WrapperInstruction(SymbolCallable associatedCallable, SymbolVariable resultToWrap) {
        super(associatedCallable, Optional.empty());
        this.result = resultToWrap;
    }

    @Override
    public String buildAssemblyString() {
        return "";
    }

    @Override
    public int getNeededStackSize() {
        return 1; // will be needed at for loading the constant
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        return List.of(this.result);
    }
}
