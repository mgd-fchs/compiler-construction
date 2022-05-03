package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.List;
import java.util.Optional;

public class WhileInstruction extends ConditionalInstruction{


    public WhileInstruction(SimpleCallable associatedCallable, List<BaseInstruction> conditionals, List<BaseInstruction> ifInstructions) {
        super(associatedCallable, conditionals, ifInstructions, Optional.empty());
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        SymbolVariable resultCondition = conditionals.get(conditionals.size() - 1).result;
        String start_label = associatedCallable.associatedSymbolClass.getNextLabelCount();
        String end_label = associatedCallable.associatedSymbolClass.getNextLabelCount();

        builder.append(start_label).append(":\n");
        conditionals.forEach(instructions -> builder.append(instructions.buildAssemblyString()));
        builder.append(pushVariableOntoStack(resultCondition));
        builder.append("    ifeq ").append(end_label).append("\n");
        ifInstructions.forEach(instructions -> builder.append(instructions.buildAssemblyString()));
        builder.append("    goto ").append(start_label).append("\n");
        builder.append(end_label).append(":").append("\n\n");

        return builder.toString();
    }

    @Override
    public int getNeededStackSize() {
        int cond_stack = conditionals.stream()
                .mapToInt(BaseInstruction::getNeededStackSize)
                .max()
                .orElse(0);

        int if_stack = ifInstructions.stream()
                .mapToInt(BaseInstruction::getNeededStackSize)
                .max()
                .orElse(0);

        return cond_stack + if_stack;
    }
}
