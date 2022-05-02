package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.ClassWriter;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public abstract class BaseInstruction {
    protected final SimpleCallable associatedCallable;
    protected SymbolVariable result;

    public BaseInstruction(SimpleCallable associatedCallable, Optional<SymbolVariable> result) {
        this.associatedCallable = associatedCallable;
        this.result = result.map(associatedCallable::getNewTempSymbolVariable).orElse(null);
    }

    protected String pushVariableOntoStack(SymbolVariable variable) {
        StringBuilder builder = new StringBuilder();

        if (variable.getValue() != null) {
            builder.append("    ldc ").append(variable.getValue());
        } else {
            if (variable.getType() == PRIMITIVE) {
                switch ((SymbolPrimitiveType) variable.getActualType()) {
                    case INT:
                    case BOOL:
                        builder.append("    iload ");
                        break;
                    case STRING:
                        builder.append("    aload ");
                        break;
                    case NIX:
                        return builder.append("    aconst_null\n").toString();
                    case FLOAT:
                    case CHAR:
                    default:
                        System.out.printf(
                                "pushVariableOntoStack\n\n%n", ClassWriter.class.getName());
                        throw new RuntimeException();
                }
            } else {
                builder.append("    aload ");
            }

            int localArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(variable);

            builder.append(localArrayIndex);
        }

        return builder.append("\n").toString();
    }

    protected String popVariableFromStack(SymbolVariable variable) {
        StringBuilder builder = new StringBuilder();

        if (variable.getType() == PRIMITIVE) {
            switch ((SymbolPrimitiveType) variable.getActualType()) {
                case INT:
                case BOOL:
                    builder.append("    istore ");
                    break;
                case STRING:
                    builder.append("    astore ");
                    break;
                case NIX:
                case FLOAT:
                case CHAR:
                default:
                    System.out.printf("popVariableFromStack\n\n%n", ClassWriter.class.getName());
                    throw new RuntimeException();
            }
        } else {
            builder.append("    astore ");
        }

        int localArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(variable);
        return builder.append(localArrayIndex).append("\n").toString();
    }

    public SymbolVariable getResult() {
        return result;
    }

    /**
     * This function should be implemented so that the result of the instruction is
     * saved into the local array. The index for SymbolVariables can be found via the
     * mapping which is saved in th SimpleCallable
     *
     * @return
     */
    public abstract String buildAssemblyString();
}
