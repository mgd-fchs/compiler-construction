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
        if (returnValue.getValue() != null && (Integer) returnValue.getValue() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(pushVariableOntoStack(returnValue));
        builder.append("    invokestatic java/lang/System/exit(I)V\n");

        return  builder.toString();
    }
}
