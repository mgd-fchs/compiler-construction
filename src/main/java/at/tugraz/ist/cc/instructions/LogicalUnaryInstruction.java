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
        if (operator == null) {
            return "";
        }

        long trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        long endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(parameter));
        builder.append("   ifeq ").append(trueLabel).append("\n");
        builder.append("   ldc 0").append("\n");
        builder.append(popVariableFromStack(result));
        builder.append("   goto ").append(endLabel).append("\n");
        builder.append("   ").append(trueLabel).append(":").append("\n");
        builder.append("   ldc 1").append("\n");
        builder.append(popVariableFromStack(result));
        builder.append("   ").append(endLabel).append(":").append("\n\n");

        return builder.toString();
    }
}
