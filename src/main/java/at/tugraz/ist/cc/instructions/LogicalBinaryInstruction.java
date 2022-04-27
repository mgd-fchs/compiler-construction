package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class LogicalBinaryInstruction extends BinaryInstruction {

    public LogicalBinaryInstruction(SimpleCallable associatedCallable, SymbolVariable leftParameter,
                                    SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL),
                leftParameter, rightParameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        int leftLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(leftParam);
        int rightLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(rightParam);
        int resultLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);
        // TODO

        return "";
    }
}
