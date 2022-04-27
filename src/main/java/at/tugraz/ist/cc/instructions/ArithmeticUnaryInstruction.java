package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class ArithmeticUnaryInstruction extends UnaryInstruction {

    public ArithmeticUnaryInstruction(SimpleCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT), parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        int localParameterIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(parameter);
        int resultParamterIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);
        if (operator.equals(OperatorTypes.ADD)) {
            result = parameter; // TODO reuse temp
            return "";
        } else {
            return String.format("" +
                            "    iload %d        ; load from local\n" +
                            "    ineg            ; toggle sign\n" +
                            "    istore %d       ; save result into local\n\n",
                    localParameterIndex, resultParamterIndex);
        }
    }
}
