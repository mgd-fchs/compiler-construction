package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class ReturnMainInstruction extends BaseInstruction {

    private SymbolVariable returnValue;

    public ReturnMainInstruction(ReturnInstruction instruction) {
        super(instruction.associatedCallable, Optional.empty());
        this.returnValue = instruction.getReturnValue();
    }

    @Override
    public String buildAssemblyString() {
        String trueLabel = associatedCallable.associatedSymbolClass.getNextLabelCount();

        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(returnValue));
        builder.append("    ifeq ").append(trueLabel).append("\n");
        builder.append(pushVariableOntoStack(returnValue));
        builder.append("    invokestatic java/lang/System/exit(I)V\n");
        builder.append(trueLabel).append(":").append("\n");

        return  builder.toString();
    }

    @Override
    public int getNeededStackSize() {
        return 1;
    }
}
