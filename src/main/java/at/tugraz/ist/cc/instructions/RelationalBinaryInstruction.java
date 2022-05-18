package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class RelationalBinaryInstruction extends BinaryInstruction {

    public RelationalBinaryInstruction(SymbolCallable associatedCallable, SymbolVariable leftParameter,
                                       SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL),
                leftParameter, rightParameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        /**
         if_icmpeq succeeds if and only if value1 = value2
         if_icmpne succeeds if and only if value1 ≠ value2
         if_icmplt succeeds if and only if value1 < value2
         if_icmple succeeds if and only if value1 ≤ value2
         if_icmpgt succeeds if and only if value1 > value2
         if_icmpge succeeds if and only if value1 ≥ value2
         **/

        StringBuilder builder = new StringBuilder();
        String endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        String trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        builder
                .append(pushVariableOntoStack(leftParam))
                .append(pushVariableOntoStack(rightParam))
                .append("    ").append(CodeGeneratorUtils.getOpAssembly(operator)).append(" ").append(trueLabel).append("\n")
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
