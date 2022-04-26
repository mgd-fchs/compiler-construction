package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class BinaryInstruction extends BaseInstruction {
    private SymbolVariable leftParam;
    private SymbolVariable rightParam;
    private OperatorTypes operator;

    public BinaryInstruction(SimpleCallable associatedCallable, SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT))); // TODO automatic casting ??
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }

    @Override
    public String buildAssemblyString() {
        int leftLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(leftParam);
        int rightLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(rightParam);
        int resultLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);
        // TODO const value are not handled
        // TODO not all ops are handled
        return String.format("" +
                        "   iload %d                ; pushes from local pos\n" +
                        "   iload %d                ; pushes from local pos\n" +
                        "   %s                      ; performs binary op\n" +
                        "   istore %d               ; pops result from stack and saves into local pos\n\n",
                leftLocalArrayIndex, rightLocalArrayIndex,
                CodeGeneratorUtils.getOpAssembly(operator), resultLocalArrayIndex);
    }
}
