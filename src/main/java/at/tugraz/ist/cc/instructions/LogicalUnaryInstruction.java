package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class LogicalUnaryInstruction extends UnaryInstruction {

    public LogicalUnaryInstruction(SimpleCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL), parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        int localParameterIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(parameter);
        int resultParamterIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);

        long trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        long endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();


        return String.format("" +
                        "    iload %d           ; load from local\n" +
                        "    ifeq %d            ;\n" +
                        "    ldc 0              ;\n" +
                        "    istore %d          ;\n" +
                        "    goto   %d          ;\n" +
                        "    %d:                ;\n" +
                        "    ldc 1              ;\n" +
                        "    istore %d          ;\n" +
                        "    %d:                ;\n\n",
                localParameterIndex, trueLabel, resultParamterIndex, endLabel, trueLabel, resultParamterIndex, endLabel);
    }
}
