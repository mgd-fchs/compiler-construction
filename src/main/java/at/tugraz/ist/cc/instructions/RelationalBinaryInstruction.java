package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class RelationalBinaryInstruction extends BinaryInstruction {

    public RelationalBinaryInstruction(SimpleCallable associatedCallable, SymbolVariable leftParameter,
                                       SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL),
                leftParameter, rightParameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        int leftLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(leftParam);
        int rightLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(rightParam);
        int resultLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);


        /**
         if_icmpeq succeeds if and only if value1 = value2
         if_icmpne succeeds if and only if value1 ≠ value2
         if_icmplt succeeds if and only if value1 < value2
         if_icmple succeeds if and only if value1 ≤ value2
         if_icmpgt succeeds if and only if value1 > value2
         if_icmpge succeeds if and only if value1 ≥ value2
         **/

        long endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        long trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        return String.format("" +
                        "   iload %d                ; \n" +
                        "   iload %d                ; \n" +
                        "   %s %d                   ; \n" +
                        "   ldc 0                   ; \n" +
                        "   istore %s               ; \n" +
                        "   goto %d                 ; \n" +
                        "   %d: ldc 1               ; \n" +
                        "   istore %s               ; \n" +
                        "   %d:                     ; \n\n",
                leftLocalArrayIndex, rightLocalArrayIndex, CodeGeneratorUtils.getOpAssembly(operator),
                trueLabel, resultLocalArrayIndex, endLabel, trueLabel, resultLocalArrayIndex, endLabel);
    }
}
