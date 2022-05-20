package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public abstract class BinaryInstruction extends BaseInstruction {
    public SymbolVariable leftParam;
    public SymbolVariable rightParam;
    public OperatorTypes operator;

    public BinaryInstruction(SymbolCallable associatedCallable, SymbolVariable result,
                             SymbolVariable leftParameter, SymbolVariable rightParameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(result));
        this.leftParam = leftParameter;
        this.rightParam = rightParameter;
        this.operator = operator;
    }

    @Override
    public int getNeededStackSize() {
        return 2;
    }

    public void setLhs(SymbolVariable lhs) {
        leftParam = lhs;
    }

    public void setRhs(SymbolVariable rhs) {
        rightParam = rhs;
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        Collection<SymbolVariable> usedVariablesOnLocal =  new ArrayList<>();

        if (leftParam.getValue() == null) {
            usedVariablesOnLocal.add(leftParam);
        }

        if (leftParam.getValue() == null) {
            usedVariablesOnLocal.add(rightParam);
        }

        usedVariablesOnLocal.add(result);

        return usedVariablesOnLocal;
    }
}
