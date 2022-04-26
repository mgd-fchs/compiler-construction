package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class MethodInvocationInstruction extends BaseInstruction {
    private SymbolClass classType;
    private SimpleCallable invokedMethod;

    // TODO: print/read -> only differentiate in write-funtion since jasmin command is "invokevirtual" in any case?
    public MethodInvocationInstruction(SimpleCallable associatedCallable, SymbolClass classType, SimpleCallable invokedMethod) {
        super(associatedCallable, Optional.of(invokedMethod.getReturnValue()));
        this.classType = classType;
        this.invokedMethod = invokedMethod;

    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
