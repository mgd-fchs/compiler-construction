package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReturnMainInstruction extends BaseInstruction {

    private final SymbolVariable returnValue;

    public ReturnMainInstruction(ReturnInstruction instruction) {
        super(instruction.associatedCallable, Optional.empty());
        this.returnValue = instruction.getReturnValue();
    }

    @Override
    public String buildAssemblyString() {

        if (returnValue.getValue() != null &&  (Integer) returnValue.getValue() == 0) {
            return "";
        } else {
            String trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

            StringBuilder builder = new StringBuilder();

            if (returnValue.getValue() == null) {
                builder.append(pushVariableOntoStack(returnValue));
                builder.append("    ifeq ").append(trueLabel).append("\n");
                builder.append(pushVariableOntoStack(returnValue));
                builder.append("    invokestatic java/lang/System/exit(I)V\n");
                builder.append(trueLabel).append(":").append("\n");
            } else {
                builder.append(pushVariableOntoStack(returnValue));
                builder.append("    invokestatic java/lang/System/exit(I)V\n");
            }


            return  builder.toString();
        }
    }

    @Override
    public int getNeededStackSize() {
        return (returnValue.getValue() != null && (Integer) returnValue.getValue() == 0) ? 0 : 1;
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        return (returnValue.getValue() == null) ? List.of(returnValue) : Collections.emptyList();
    }
}
