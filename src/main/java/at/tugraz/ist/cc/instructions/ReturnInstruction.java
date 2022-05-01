package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.ClassWriter;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class ReturnInstruction extends BaseInstruction {
    private SymbolVariable returnValue;

    public ReturnInstruction(SimpleCallable associatedCallable, SymbolVariable returnValue) {
        super(associatedCallable, Optional.of(returnValue));
        this.returnValue = returnValue;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(returnValue));

        if (returnValue.getType() == PRIMITIVE) {
            switch ((SymbolPrimitiveType) returnValue.getActualType()) {
                case INT:
                case BOOL:
                    builder.append("    ireturn");
                    break;
                case STRING:
                case NIX:
                    builder.append("    areturn ");
                    break;
                case FLOAT:
                case CHAR:
                default:
                    System.out.printf(
                            "pushVariableOntoStack\n\n%n", ClassWriter.class.getName());
                    throw new RuntimeException();
            }
        } else {
            builder.append("    areturn ");
        }

        builder.append("\n");

        return builder.toString();
    }
}
