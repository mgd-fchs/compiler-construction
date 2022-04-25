package at.tugraz.ist.cc;

import at.tugraz.ist.cc.symbol_table.*;

import java.util.List;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class CodeAssembly {

    public static String getClassHead(String className) {
        /**
         * e.g.
         * .source noSource
         * .class public HansPeterClass
         * .super java/lang/Object
         */
        return String.format(".source noSource\n" +
                ".class public %s\n" +
                ".super java/lang/Object\n", className);
    }

    private static String getTypeAsString(SymbolVariable symbolVariable) {
        if (symbolVariable.getType() == PRIMITIVE) {
            switch ((SymbolPrimitiveType) symbolVariable.getActualType()) {
                case INT:
                    return "I";
                case BOOL:
                    return "Z";
                case STRING:
                    return "Ljava/lang/String";
                case NIX:
                case FLOAT:
                case CHAR:
                default:
                    System.out.printf(
                            "Not supported type in class %s, method getField()\n\n%n", CodeAssembly.class.getName());
                    System.exit(100);
                    return null;
            }
        } else {
            return symbolVariable.getTypeAsString();
        }
    }

    public static String getField(SymbolModifier modifier, SymbolVariable symbolVariable) {
        /** e.g. ".field private hans_peter_field I" */
        return String.format(".field %s %s %s",
                modifier.toString(), symbolVariable.getName(), getTypeAsString(symbolVariable));
    }

    public static String getDefaultConstructor() {
        return ".method public <init>()V\n" +
                ".limit stack 1\n" +
                ".limit locals 1\n" +
                "  aload_0\n" +
                "  invokespecial java/lang/Object/<init>()V\n" +
                "  return\n" +
                ".end method\n\n";
    }

    public static String getConstructor(SymbolConstructor symbolConstructor) {
        String parameter = getParameter(symbolConstructor.getParams());
        return String.format(
                ".method public <init>(%s)V\n" +
                        ".limit stack 1\n" +
                        ".limit locals 1\n" +
                        "  aload_0\n" +
                        "  invokespecial java/lang/Object/<init>()V\n" +
                        "  return\n" +
                        ".end method\n\n",
                parameter);
    }

    private static String getParameter(List<SymbolVariable> params) {
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach(param -> stringBuilder.append(getTypeAsString(param)).append(";"));
        return stringBuilder.toString();
    }

    public static String getMethod(SymbolMethod symbolMethod) {
        return null;
    }
}
