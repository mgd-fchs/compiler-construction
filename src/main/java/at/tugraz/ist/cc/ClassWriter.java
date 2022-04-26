package at.tugraz.ist.cc;

import at.tugraz.ist.cc.symbol_table.*;

import java.io.*;
import java.util.Collection;
import java.util.List;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class ClassWriter implements AutoCloseable {
    private final PrintWriter writer;
    private final SymbolClass symbolClass;


    public ClassWriter(String out_path, SymbolClass symbolClass) throws IOException {
        String fileName = out_path + "/" + symbolClass.getClassName() + ".j";
        File file = new File(fileName);
        file.getParentFile().mkdirs();

        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        this.writer = new PrintWriter(bufferedWriter);
        this.symbolClass = symbolClass;
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    public void write() {
        writeClassHeader();

        if (symbolClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME)) {
            symbolClass.getMethods().stream().findFirst().ifPresent(symbolMethod -> {
                writeMainMethod(symbolMethod);
                return;
            });
        }

        symbolClass.getMembers().forEach(member -> writeMember(member.getKey(), member.getValue()));
        writer.println("");

        if (symbolClass.hasNoEmptyConstructor()) {
            writeDefaultCtor();
        }

        symbolClass.getConstructors().forEach(this::writeCtor);
        symbolClass.getMethods().forEach(this::writeMethod);
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
                            "Not supported type in class %s, method getField()\n\n%n", ClassWriter.class.getName());
                    System.exit(100);
                    return null;
            }
        } else {
            return symbolVariable.getTypeAsString();
        }
    }

    private void writeClassHeader() {
        /**
         * e.g.
         * .source noSource
         * .class public HansPeterClass
         * .super java/lang/Object
         */
        writer.printf("" +
                ".source noSource\n" +
                ".class public %s\n" +
                ".super java/lang/Object\n\n", symbolClass.getClassName());
    }

    private void writeMember(SymbolModifier modifier, SymbolVariable symbolVariable) {
        /** e.g. ".field private hans_peter_field I" */
        writer.printf(".field %s %s %s\n",
                modifier.toString(), symbolVariable.getName(), getTypeAsString(symbolVariable));
    }

    private void writeBodyInstructions(Collection a) {
        //TODO:
    }

    private void writeMethod(SymbolMethod symbolMethod) {
        String parameter = getParameter(symbolMethod.getParams());
        int stack_limit = 1; // TODO
        int local_limit = 1; // TODO
        writer.printf("" +
                        ".method %s %s(%s)V\n" +
                        ".limit stack %d\n" +
                        ".limit locals %d\n",
                symbolMethod.getAccessSymbol().toString(), symbolMethod.getName(), parameter, stack_limit, local_limit);

        writeBodyInstructions(null);

        writer.printf("" +
                "  aload_0\n" + // TODO right return value
                "  return\n" +
                ".end method\n\n");
    }

    private void writeMainMethod(SymbolMethod symbolMethod) {
        int stack_limit = 1; // TODO
        int local_limit = 1; // TODO
        writer.printf("" +
                ".method public static main([Ljava/lang/String;)V\n" +
                ".limit stack %d\n" +
                ".limit locals %d\n", stack_limit, local_limit);

        writeBodyInstructions(null);

        writer.printf("" +
                "  return\n" +
                ".end method\n\n");
    }

    private void writeDefaultCtor() {
        writer.printf("" +
                ".method public <init>()V\n" +
                ".limit stack 1\n" +
                ".limit locals 1\n" +
                "  aload_0\n" +
                "  invokespecial java/lang/Object/<init>()V\n" +
                "  return\n" +
                ".end method\n\n");
    }

    private void writeCtor(SymbolConstructor symbolConstructor) {
        String parameter = getParameter(symbolConstructor.getParams());
        int stack_limit = 1; // TODO
        int local_limit = 1; // TODO
        writer.printf("" +
                ".method public <init>(%s)V\n" +
                ".limit stack %d\n" +
                ".limit locals %d\n", parameter, stack_limit, local_limit);

        writeBodyInstructions(null);

        writer.printf("" +
                "  aload_0\n" +
                "  invokespecial java/lang/Object/<init>()V\n" +
                "  return\n" +
                ".end method\n\n");
    }

    private static String getParameter(List<SymbolVariable> params) {
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach(param -> stringBuilder.append(getTypeAsString(param)).append(";"));
        return stringBuilder.toString();
    }

    private void printInt(int localArrayIndex) {
        writer.printf("" +
                "  getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "  iload %s\n" +
                "  invokevirtual java/io/PrintStream/print(I)V\n" +
                "  ldc 0\n", localArrayIndex);
    }

    private void printString(int localArrayIndex) {
        writer.printf("" +
                "  getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "  aload %s\n" +
                "  invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V\n" +
                "  ldc 0\n", localArrayIndex);
    }

    private void printBool(int localArrayIndex) {
        writer.printf("" +
                "  getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "  iload %s\n" +    // TODO: maybe iload is not right for bool
                "  invokevirtual java/io/PrintStream/print(Z)V\n" +
                "  ldc 0\n", localArrayIndex);
    }

    private void readInt(int localArrayIndex) {
        // https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
        // https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html
        writer.printf("" +
                "  new java/util/Scanner\n" +
                "  dup\n" +
                "  getstatic java/lang/System/in Ljava/io/InputStream;\n" +
                "  invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n" +
                "  invokevirtual java/util/Scanner/nextInt()I\n" +
                "  istore %s", localArrayIndex);
    }

    private void readString(int localArrayIndex) {
        writer.printf("" +
                "  new java/util/Scanner\n" +
                "  dup\n" +
                "  getstatic java/lang/System/in Ljava/io/InputStream;\n" +
                "  invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n" +
                "  invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;\n" +
                "  astore %s", localArrayIndex);
    }
}
