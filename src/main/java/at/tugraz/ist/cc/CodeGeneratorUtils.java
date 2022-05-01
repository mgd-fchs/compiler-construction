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
            return symbolVariable.getTypeAsString() + ";";
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

    public static String printInt(SimpleCallable associatedCallable,
                                  SymbolVariable printVariable, SymbolVariable returnVariable) {
        int localArrayIndexParam = associatedCallable.getLocalArrayIndexBySymbolVariable(printVariable);
        int localArrayIndexReturn = associatedCallable.getLocalArrayIndexBySymbolVariable(returnVariable);
        return String.format("" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload %s\n" +
                "   invokevirtual java/io/PrintStream/print(I)V\n" +
                "   ldc 0\n" +
                "   istore %d\n\n", localArrayIndexParam, localArrayIndexReturn);
    }

    public static String printString(SimpleCallable associatedCallable,
                                     SymbolVariable printVariable, SymbolVariable returnVariable) {
        int localArrayIndexParam = associatedCallable.getLocalArrayIndexBySymbolVariable(printVariable);
        int localArrayIndexReturn = associatedCallable.getLocalArrayIndexBySymbolVariable(returnVariable);
        return String.format("" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   aload %s\n" +
                "   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V\n" +
                "   ldc 0\n" +
                "   istore %d\n\n", localArrayIndexParam, localArrayIndexReturn);
    }

    public static String printBool(SimpleCallable associatedCallable,
                                   SymbolVariable printVariable, SymbolVariable returnVariable) {
        int localArrayIndexParam = associatedCallable.getLocalArrayIndexBySymbolVariable(printVariable);
        int localArrayIndexReturn = associatedCallable.getLocalArrayIndexBySymbolVariable(returnVariable);
        return String.format("" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload %s\n" +    // TODO: maybe iload is not right for bool
                "   invokevirtual java/io/PrintStream/print(Z)V\n" +
                "   ldc 0\n" +
                "   istore %d\n\n", localArrayIndexParam, localArrayIndexReturn);
    }

    public static String readInt(SimpleCallable associatedCallable, SymbolVariable printVariable) {
        // https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
        // https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html
        int localArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(printVariable);
        return String.format("" +
                "   new java/util/Scanner\n" +
                "   dup\n" +
                "   getstatic java/lang/System/in Ljava/io/InputStream;\n" +
                "   invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n" +
                "   invokevirtual java/util/Scanner/nextInt()I\n" +
                "   istore %s", localArrayIndex);
    }

    public static String readString(SimpleCallable associatedCallable, SymbolVariable printVariable) {
        int localArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(printVariable);
        return String.format("" +
                "  new java/util/Scanner\n" +
                "  dup\n" +
                "  getstatic java/lang/System/in Ljava/io/InputStream;\n" +
                "  invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n" +
                "  invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;\n" +
                "  astore %s", localArrayIndex);
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
                return (left.getType() == CLASS) ?
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
