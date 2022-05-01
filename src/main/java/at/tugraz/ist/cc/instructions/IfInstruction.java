package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.List;
import java.util.Optional;

public class IfInstruction extends ConditionalInstruction{

    public final List<BaseInstruction> elseInstructions;

    public IfInstruction(SimpleCallable currentCallable, List<BaseInstruction> conditionalExpression,
                         List<BaseInstruction> ifInstructions, List<BaseInstruction> elseInstructions) {
        super(currentCallable, conditionalExpression, ifInstructions,
                Optional.empty());

        this.elseInstructions = elseInstructions;
    }

    @Override
    public String buildAssemblyString() {
        StringBuilder builder = new StringBuilder();
        SymbolVariable resultCondition = conditionals.get(conditionals.size() - 1).result;
        String else_label = associatedCallable.associatedSymbolClass.getNextLabelCount();
        String end_label = associatedCallable.associatedSymbolClass.getNextLabelCount();

        conditionals.forEach(instructions -> builder.append(instructions.buildAssemblyString()));
        builder.append(pushVariableOntoStack(resultCondition));
        builder.append("    ifeq ").append(else_label).append("\n");
        ifInstructions.forEach(instructions -> builder.append(instructions.buildAssemblyString()));
        builder.append("    goto ").append(end_label).append("\n");
        builder.append(else_label).append(":\n");
        elseInstructions.forEach(instructions -> builder.append(instructions.buildAssemblyString()));
        builder.append(end_label).append(":").append("\n\n");

        return builder.toString();
    }

}
