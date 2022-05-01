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
        StringBuilder builder = new StringBuilder();

        builder.append(pushVariableOntoStack(classRef));
        params.forEach(param -> builder.append(pushVariableOntoStack(param)));
        builder.append("    invokespecial ")
                .append(classRef.getName())
                .append("/")
                .append(invokedMethod.getName())
                .append("(")
                .append(CodeGeneratorUtils.getParameterTypesAsString(params))
                .append(")")
                .append(CodeGeneratorUtils.getTypeAsAssemblyString(invokedMethod.getReturnValue()))
                .append("\n")
                .append(popVariableFromStack(result));

        return builder.toString();
    }
}
