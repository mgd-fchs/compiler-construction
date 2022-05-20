package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class AllocInstruction extends BaseInstruction {
    private final SymbolVariable actualType;
    private final Collection<SymbolVariable> params;

    public AllocInstruction(SymbolCallable associatedCallable,
                            SymbolVariable classType, Collection<SymbolVariable> params) {
        super(associatedCallable, Optional.of(classType));
        this.actualType = classType;
        this.params = params;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();

        builder.append("    new ")
                .append(actualType.getTypeAsString()).append("\n")
                .append("    dup\n");
        params.forEach(param -> builder.append(pushVariableOntoStack(param)));
        builder.append("    invokespecial ")
                .append(actualType.getTypeAsString())
                .append("/")
                .append("<init>")
                .append("(")
                .append(CodeGeneratorUtils.getParameterTypesAsString(params))
                .append(")")
                .append("V")
                .append("\n")
                .append(popVariableFromStack(result));

        return builder.toString();

    }

    @Override
    public int getNeededStackSize() {
        return 2 + params.size();
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        Collection<SymbolVariable> usedVariablesOnLocal = new ArrayList<>();

        params.forEach(param -> {
            if (param.getValue() == null) {
                usedVariablesOnLocal.add(param);
            }
        });

        usedVariablesOnLocal.add(result);

        return usedVariablesOnLocal;
    }
}
