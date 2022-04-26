package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Collection;
import java.util.Optional;

public class MethodInvocationInstruction extends BaseInstruction {
    private final SymbolVariable classRef;
    private final SimpleCallable invokedMethod;

    private final Collection<SymbolVariable> params;

    // TODO: print/read -> only differentiate in write-funtion since jasmin command is "invokevirtual" in any case?
    public MethodInvocationInstruction(SimpleCallable associatedCallable, SymbolVariable classRef,
                                       SimpleCallable invokedMethod, Collection<SymbolVariable> params) {
        super(associatedCallable, Optional.of(invokedMethod.getReturnValue()));
        this.classRef = classRef;
        this.invokedMethod = invokedMethod;
        this.params = params;
    }

    @Override
    public String buildAssemblyString() {
        int classRefLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(classRef);
        int resultLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(classRef);

        StringBuilder builder = new StringBuilder();

        builder
                // load obj-ref
                .append("    aload ")
                .append(classRefLocalArrayIndex)
                .append("           ; loading the obj-ref for the method call\n")
                // load params
                .append(CodeGeneratorUtils.getLoadingParametersString(params, associatedCallable))
                .append("   invokespecial ")
                .append(String.format("" +
                                "   invokespecial %s/%s(%s)V         ; calls the constructor and pops a obj-ref from stack\n"
                                , classRef.getName(), invokedMethod.getName(), CodeGeneratorUtils.getParameterTypesAsString(params)
                        ))
                .append((invokedMethod.getReturnValue().getType() == SymbolType.CLASS) ? "    astore " : "    istore ")
                .append(resultLocalArrayIndex);

        return builder.toString();
    }
}
