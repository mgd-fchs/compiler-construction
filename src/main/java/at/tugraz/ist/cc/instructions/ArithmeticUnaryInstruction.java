package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class ArithmeticUnaryInstruction extends UnaryInstruction{

    public ArithmeticUnaryInstruction(SimpleCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT) , parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        // TODO
        return null;
    }
}
