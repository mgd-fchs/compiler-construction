package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolMethod;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.*;

public class MethodInvocationInstruction extends BaseInstruction {
    private final SymbolVariable classRef;
    private final SymbolCallable invokedMethod;

    public List<SymbolVariable> getParams() {
        return params;
    }

    private final List<SymbolVariable> params;

    private boolean ignoreResult;

    public MethodInvocationInstruction(SymbolCallable associatedCallable, SymbolVariable classRef,
                                       SymbolCallable invokedMethod, List<SymbolVariable> params) {
        super(associatedCallable, Optional.of(invokedMethod.getReturnValue()));
        this.classRef = classRef;
        this.invokedMethod = invokedMethod;
        this.params = params;
        this.ignoreResult = false;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();

        if (invokedMethod.associatedSymbolClass == null) {
            // this must be a predefined method
            if (Objects.equals(invokedMethod.getName(), SymbolMethod.PRINT)) {
                if (params.size() != 1) {
                    throw new RuntimeException();
                }

                SymbolVariable param = params.get(0);

                builder.append("    getstatic java/lang/System/out Ljava/io/PrintStream;\n")
                        .append(pushVariableOntoStack(param))
                        .append("    invokevirtual java/io/PrintStream/print(")
                        .append(CodeGeneratorUtils.getTypeAsAssemblyString(param))
                        .append(")V\n");
                if (!ignoreResult) {
                    builder.append("    ldc 0\n");
                }
            } else {
                if (params.size() != 0) {
                    throw new RuntimeException();
                }

                builder.append("" +
                        "    new java/util/Scanner\n" +
                        "    dup\n" +
                        "    getstatic java/lang/System/in Ljava/io/InputStream;\n" +
                        "    invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n");

                if (Objects.equals(invokedMethod.getName(), SymbolMethod.READ_STRING)) {
                    builder.append("    invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;\n");
                } else if (Objects.equals(invokedMethod.getName(), SymbolMethod.READ_INT)) {
                    builder.append("    invokevirtual java/util/Scanner/nextInt()I\n");
                } else {
                    throw new RuntimeException();
                }
            }
        } else {
            builder.append(pushVariableOntoStack(classRef));
            params.forEach(param -> builder.append(pushVariableOntoStack(param)));
            builder.append("    invokevirtual ")
                    .append(classRef.getTypeAsString())
                    .append("/")
                    .append(invokedMethod.getName())
                    .append("(")
                    .append(CodeGeneratorUtils.getParameterTypesAsString(params))
                    .append(")")
                    .append(CodeGeneratorUtils.getTypeAsAssemblyString(invokedMethod.getReturnValue()))
                    .append("\n");
        }

        if (!ignoreResult) {
            builder.append(popVariableFromStack(result));
        }

        builder.append("\n\n");

        return builder.toString();
    }

    @Override
    public int getNeededStackSize() {
        int stack_size;
        if (invokedMethod.associatedSymbolClass == null) {
            if (Objects.equals(invokedMethod.getName(), SymbolMethod.PRINT)) {
                // print
                stack_size = 1 + 1; // getstatic + param
            } else {
                // read
                stack_size = 1 + 1 + 1; // scanner + dup
            }

        } else {
            stack_size = 1 + params.size();
        }

        return stack_size;
    }

    public boolean isReadOrPrint() {
        return classRef.getName() == null;
    }


    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        Collection<SymbolVariable> usedVariablesOnLocal = new ArrayList<>();

        params.forEach(param -> {
            if (param.getValue() == null) {
                usedVariablesOnLocal.add(param);
            }
        });

        if (classRef.getName() != null) {
            // print or read method
            usedVariablesOnLocal.add(classRef);
            if (!ignoreResult) {
                usedVariablesOnLocal.add(result);
            }
        } else {
            usedVariablesOnLocal.add(result);
        }

        return usedVariablesOnLocal;
    }

    public void setIgnoreResult() {
        ignoreResult = true;
    }
}
