package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class MethodInvocationInstruction extends BaseInstruction {
    private SymbolClass classType;
    private SimpleCallable invokedMethod;

    // TODO: print/read -> only differentiate in write-funtion since jasmin command is "invokevirtual" in any case?
    public MethodInvocationInstruction(SymbolClass classType, SimpleCallable invokedMethod) {
        this.classType = classType;
        this.invokedMethod = invokedMethod;

    }

    @Override
    public String buildAssembly() {
        // TODO
        return null;
    }
}
