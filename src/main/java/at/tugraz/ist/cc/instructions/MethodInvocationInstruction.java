package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class MethodInvocationInstruction {
    private SymbolClass classType;
    private SimpleCallable invokedMethod;

    public MethodInvocationInstruction(SymbolClass classType, SimpleCallable invokedMethod) {

        this.classType = classType;
        this.invokedMethod = invokedMethod;

    }
}
