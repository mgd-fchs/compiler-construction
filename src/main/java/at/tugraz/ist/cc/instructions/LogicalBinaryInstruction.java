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
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(leftParam));

        String endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        if (operator.equals(OperatorTypes.AND)){
            String falseLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

            builder.append("   ifeq ").append(falseLabel).append("\n");
            builder.append(pushVariableOntoStack(rightParam));
            builder.append("   ifeq ").append(falseLabel).append("\n");
            builder.append("   ldc 1").append("\n");
            builder.append(popVariableFromStack(result));
            builder.append("   goto ").append(endLabel).append("\n");
            builder.append(falseLabel).append(":").append("\n");
            builder.append("   ldc 0").append("\n");
            builder.append(popVariableFromStack(result));
            builder.append(endLabel).append(":").append("\n\n");
        } else {
            String trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

            builder.append("   ifne ").append(trueLabel).append("\n");
            builder.append(pushVariableOntoStack(rightParam));
            builder.append("   ifne ").append(trueLabel).append("\n");
            builder.append("   ldc 0").append("\n");
            builder.append(popVariableFromStack(result));
            builder.append("   goto ").append(endLabel).append("\n");
            builder.append(trueLabel).append(":").append("\n");
            builder.append("   ldc 1").append("\n");
            builder.append(popVariableFromStack(result));
            builder.append(endLabel).append(":").append("\n\n");
        }

        return builder.toString();
    }
}
