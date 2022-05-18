package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class LogicalUnaryInstruction extends UnaryInstruction {

    public LogicalUnaryInstruction(SymbolCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL), parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        String trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();
        String endLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(parameter));
        builder.append("    ifeq ").append(trueLabel).append("\n");
        builder.append("    ldc 0").append("\n");
        builder.append(popVariableFromStack(result));
        builder.append("    goto ").append(endLabel).append("\n");
        builder.append(trueLabel).append(":").append("\n");
        builder.append("    ldc 1").append("\n");
        builder.append(popVariableFromStack(result));
        builder.append(endLabel).append(":").append("\n\n");

        return builder.toString();
    }
}
