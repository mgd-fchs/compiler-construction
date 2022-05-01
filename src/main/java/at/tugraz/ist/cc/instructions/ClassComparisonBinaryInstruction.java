package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class ClassComparisonBinaryInstruction extends BinaryInstruction {

    public ClassComparisonBinaryInstruction(SimpleCallable associatedCallable, SymbolVariable leftParameter,
                                            SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL),
                leftParameter, rightParameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        /**
         * if_acmpeq succeeds if and only if value1 = value2
         * if_acmpne succeeds if and only if value1 ≠ value2
         */

        long endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        long trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        StringBuilder builder = new StringBuilder();

        builder
                .append(pushVariableOntoStack(leftParam))
                .append(pushVariableOntoStack(rightParam))
                .append("    ").append( (operator.equals(OperatorTypes.EQUAL)) ? "if_acmpeq" : "if_acmpne").append(" ").append(trueLabel).append("\n")
                .append("   ldc 0\n")
                .append(popVariableFromStack(result))
                .append("   goto ").append(endLabel).append("\n")
                .append(trueLabel).append(":\n")
                .append("   ldc 1\n")
                .append(popVariableFromStack(result))
                .append(endLabel).append(":\n")
                .append("\n\n");

        return builder.toString();
    }
}
