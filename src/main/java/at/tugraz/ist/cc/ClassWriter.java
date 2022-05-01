package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.BaseInstruction;
import at.tugraz.ist.cc.instructions.ReturnInstruction;
import at.tugraz.ist.cc.instructions.ReturnMainInstruction;
import at.tugraz.ist.cc.symbol_table.*;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
            Optional<SymbolMethod> method = symbolClass.getMethods().stream().findFirst();
            if (method.isPresent()) {
                writeMainMethod(method.get());
                return;
            }
        }

        symbolClass.getMembers().forEach(member -> writeMember(member.getKey(), member.getValue()));
        writer.println("");

        if (symbolClass.hasNoEmptyConstructor()) {
            writeDefaultCtor();
        }

        symbolClass.getConstructors().forEach(this::writeCtor);
        symbolClass.getMethods().forEach(this::writeMethod);
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
                modifier.toString(), symbolVariable.getName(), CodeGeneratorUtils.getTypeAsAssemblyString(symbolVariable));
    }

    private void writeBodyInstructions(Collection a) {
        //TODO:
    }

    private void writeMethod(SymbolMethod symbolMethod) {
        String parameter = CodeGeneratorUtils.getParameterTypesAsString(symbolMethod.getParams());
        int stack_limit = 100; // TODO
        int local_limit = 100; // TODO
        writer.printf("" +
                        ".method %s %s(%s)V\n" +
                        ".limit stack %d\n" +
                        ".limit locals %d\n",
                symbolMethod.getAccessSymbol().toString(), symbolMethod.getName(),
                parameter, stack_limit, local_limit);

        symbolMethod.instructions.forEach(baseInstruction ->
                writer.print(baseInstruction.buildAssemblyString()));

        writer.printf(".end method\n\n");
    }

    private void writeMainMethod(SymbolMethod symbolMethod) {
        int stack_limit = 100; // TODO
        int local_limit = 100; // TODO
        writer.printf("" +
                ".method public static main([Ljava/lang/String;)V\n" +
                ".limit stack %d\n" +
                ".limit locals %d\n", stack_limit, local_limit);

        List<BaseInstruction> instructions = symbolMethod.instructions;
        BaseInstruction instruction = instructions.get(instructions.size() - 1);

        if (instruction instanceof ReturnInstruction) {
            instructions.remove(instruction);
            ReturnMainInstruction replaced = new ReturnMainInstruction((ReturnInstruction) instruction);
            instructions.add(replaced);
        }

        symbolMethod.instructions.forEach(baseInstruction ->
                writer.print(baseInstruction.buildAssemblyString()));

        writer.printf(".end method\n\n");
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
        String parameter = CodeGeneratorUtils.getParameterTypesAsString(symbolConstructor.getParams());
        int stack_limit = 100; // TODO
        int local_limit = 100; // TODO
        writer.printf("" +
                ".method public <init>(%s)V\n" +
                ".limit stack %d\n" +
                ".limit locals %d\n", parameter, stack_limit, local_limit);

        symbolConstructor.instructions.forEach(baseInstruction ->
                writer.print(baseInstruction.buildAssemblyString()));

        writer.printf("" +
                "  aload_0\n" +
                "  invokespecial java/lang/Object/<init>()V\n" +
                "  return\n" +
                ".end method\n\n");
    }
}
