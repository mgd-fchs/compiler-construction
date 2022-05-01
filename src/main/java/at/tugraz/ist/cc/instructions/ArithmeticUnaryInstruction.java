package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class ArithmeticUnaryInstruction extends UnaryInstruction {

    public ArithmeticUnaryInstruction(SimpleCallable associatedCallable, SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT), parameter, operator);
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();

        if (operator.equals(OperatorTypes.ADD)) {
            result = parameter; // TODO reuse temp
            return "";
        } else {
            return  builder
                    .append(pushVariableOntoStack(parameter))
                    .append("    ineg").append("\n")
                    .append(popVariableFromStack(result))
                    .append("\n\n")
                    .toString();
        }
    }
}
