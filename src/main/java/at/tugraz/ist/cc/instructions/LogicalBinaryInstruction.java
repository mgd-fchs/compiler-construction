package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class LogicalBinaryInstruction  extends BinaryInstruction{
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

        long endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        if (operator.equals(OperatorTypes.AND)){
            long falseLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
            return String.format("" +
                            "   iload %d                ; pushes from local pos\n" +
                            "   ifeq %d                 ; \n" + // TODO: description
                            "   iload %d                ; \n" +
                            "   ifeq %d                 ; \n" +
                            "   ldc 1                   ; \n"+
                            "   istore %s               ; \n" +
                            "   goto %d                 ; \n"+
                            "   %d: ldc 0               ; \n" +
                            "   istore %s               ; \n" +
                            "   %d:                     ; \n\n",
                    leftLocalArrayIndex, falseLabel, rightLocalArrayIndex, falseLabel,
                    resultLocalArrayIndex, endLabel, falseLabel, resultLocalArrayIndex, endLabel);
        } else {
            long trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
            return String.format("" +
                            "   iload %d                ; pushes from local pos\n" +
                            "   ifne %d                 ; \n" + // TODO: description
                            "   iload %d                ; \n" +
                            "   ifne %d                 ; \n" +
                            "   ldc 0                   ; \n"+
                            "   istore %s               ; \n" +
                            "   goto %d                 ; \n"+
                            "   %d: ldc 1               ; \n" +
                            "   istore %s               ; \n" +
                            "   %d:                     ; \n\n",
                    leftLocalArrayIndex, trueLabel, rightLocalArrayIndex, trueLabel,
                    resultLocalArrayIndex, endLabel, trueLabel, resultLocalArrayIndex, endLabel);
        }
    }
}
