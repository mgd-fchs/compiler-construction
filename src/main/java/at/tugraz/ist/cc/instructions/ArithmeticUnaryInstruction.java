package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class ArithmeticUnaryInstruction extends UnaryInstruction {

    public ArithmeticUnaryInstruction(SymbolCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT), parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(parameter));

        if (operator.equals(OperatorTypes.SUB)) {
            builder.append("    ineg\n");
        }

        return builder.append(popVariableFromStack(result))
                .append("\n\n")
                .toString();
    }
}
