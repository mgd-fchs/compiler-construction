package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public abstract class BaseInstruction {
    protected final SimpleCallable associatedCallable;
    protected final SymbolVariable result;

    public BaseInstruction(SimpleCallable associatedCallable, Optional<SymbolVariable> result) {
        this.associatedCallable = associatedCallable;
        if (result.isPresent()){
            this.result = associatedCallable.getNewTempSymbolVariable(result.get());
        } else {
            this.result = null;
        }
    }

    /**
     * This function should be implemented so that the result of the instruction is
     * saved into the local array. The index for SymbolVariables can be found via the
     * mapping which is saved in th SimpleCallable
     * @return
     */
    public abstract String buildAssemblyString();
}
