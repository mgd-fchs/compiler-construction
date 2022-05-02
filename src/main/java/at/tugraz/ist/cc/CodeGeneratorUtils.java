package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Collection;

import static at.tugraz.ist.cc.symbol_table.SymbolType.CLASS;
import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class CodeGeneratorUtils {
    public static String getTypeAsAssemblyString(SymbolVariable symbolVariable) {
        if (symbolVariable.getType() == PRIMITIVE) {
            switch ((SymbolPrimitiveType) symbolVariable.getActualType()) {
                case INT:
                    return "I";
                case BOOL:
                    return "Z";
                case STRING:
                    return "Ljava/lang/String;";
                case NIX:
                case FLOAT:
                case CHAR:
                default:
                    System.out.printf(
                            "Not supported type in class %s, method getField()\n\n%n", ClassWriter.class.getName());
                    throw new RuntimeException();
            }
        } else {
            return "L" + symbolVariable.getTypeAsString() + ";";
        }
    }

    public static String getParameterTypesAsString(Collection<SymbolVariable> params) {
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach(param -> stringBuilder.append(getTypeAsAssemblyString(param)));
        return stringBuilder.toString();
    }

    public static String getOpAssembly(OperatorTypes type) {
        switch (type) {
            case ADD:
                return "iadd";
            case SUB:
                return "isub";
            case MUL:
                return "imul";
            case DIV:
                return "idiv";
            case MOD:
                return "irem";
            case GREATER:
                return "if_icmpgt";
            case SMALLER:
                return "if_icmplt";
            case GREATER_EQUAL:
                return "if_icmpge";
            case SMALLER_EQUAL:
                return "if_icmple";
            case EQUAL:
                return "if_icmpeq";
            case UNEQUAL:
                return "if_icmpne";
            case OR:
//                return "ior";
            case AND:
//                return "iand";
            case NOT:
                return "";
            default:
                throw new RuntimeException("getOpAssembly");
        }
    }

    public static String getLoadingParametersString(Collection<SymbolVariable> params,
                                                    SimpleCallable associatedCallable) {
        StringBuilder builder = new StringBuilder();
        params.forEach(param -> {
            int paramLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(param);
            builder
                    .append((param.getType() == SymbolType.CLASS) ? "    aload " : "    iload ")
                    .append(paramLocalArrayIndex)
                    .append("           ; loading param from local into stack\n");
        });
        return builder.toString();
    }


    public static BaseInstruction createInstruction(SimpleCallable callable, OperatorTypes op,
                                                    SymbolVariable left, SymbolVariable right) {
        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                if (right == null) {
                    // unary
                    return new ArithmeticUnaryInstruction(callable, left, op);
                }
                return new ArithmeticBinaryInstruction(callable, left, right, op);
            case GREATER:
            case SMALLER:
            case GREATER_EQUAL:
            case SMALLER_EQUAL:
                return new RelationalBinaryInstruction(callable, left, right, op);
            case EQUAL:
            case UNEQUAL:
                return (left.getType() == CLASS || left.getActualType().equals(SymbolPrimitiveType.NIX)) ?
                        new ClassComparisonBinaryInstruction(callable, left, right, op) :
                        new RelationalBinaryInstruction(callable, left, right, op);
            case OR:
            case AND:
                return new LogicalBinaryInstruction(callable, left, right, op);
            case NOT:
                return new LogicalUnaryInstruction(callable, left, op);
            default:
                throw new RuntimeException();
        }
    }
}
