package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MemberAccessInstruction extends BaseInstruction {
    public final SymbolVariable classRef;
    public final SymbolVariable memberRef;
    public SymbolVariable value;

    public MemberAccessInstruction(SymbolCallable associatedCallable, SymbolVariable classRef, SymbolVariable memberRef) {
        super(associatedCallable, Optional.of(memberRef));
        this.classRef = classRef;
        this.memberRef = memberRef;
        this.value = null;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(classRef));

        if (value != null) {
            builder.append(pushVariableOntoStack(value))
                    .append("    putfield ");
        } else {
            builder.append("    getfield ");
        }

        builder.append(classRef.getTypeAsString()).append("/")
                .append(memberRef.getName()).append(" ").append(CodeGeneratorUtils.getTypeAsAssemblyString(memberRef));

        if (value == null) {
            builder.append("\n").append(popVariableFromStack(result));
        }

        builder.append("\n\n");

        return builder.toString();
    }

    public void setPutValue(SymbolVariable value) {
        this.value = value;
    }


    @Override
    public int getNeededStackSize() {
        // get needs 1 and the put would need 2
        if (value != null) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        Collection<SymbolVariable> usedVariablesOnLocal =  new ArrayList<>();
        usedVariablesOnLocal.add(classRef);

        if (value != null) {
            if (value.getValue() == null) {
                usedVariablesOnLocal.add(value);
            }

        } else {
            usedVariablesOnLocal.add(result);
        }

        return usedVariablesOnLocal;
    }
}
