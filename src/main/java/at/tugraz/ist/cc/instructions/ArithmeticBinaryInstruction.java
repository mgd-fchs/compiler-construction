package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class ArithmeticBinaryInstruction extends BinaryInstruction {

    public ArithmeticBinaryInstruction(SymbolCallable associatedCallable, SymbolVariable leftParameter,
                                       SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT),
                leftParameter, rightParameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(pushVariableOntoStack(leftParam))
                .append(pushVariableOntoStack(rightParam))
                .append("    ").append(CodeGeneratorUtils.getOpAssembly(operator)).append("\n")
                .append(popVariableFromStack(result))
                .append("\n\n");

        return builder.toString();
    }
}
