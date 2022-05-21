package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public abstract class UnaryInstruction extends BaseInstruction {
    public SymbolVariable getParameter() {
        return parameter;
    }

    public void setParameter(SymbolVariable parameter) {
        this.parameter = parameter;
    }

    public SymbolVariable parameter;
    public OperatorTypes operator;

    public UnaryInstruction(SymbolCallable associatedCallable, SymbolVariable result,
                            SymbolVariable parameter, OperatorTypes operator) {
        super(associatedCallable, Optional.of(result));
        this.parameter = parameter;
        this.operator = operator;
    }

    @Override
    public int getNeededStackSize() {
        return 1;
    }

    @Override
    public Collection<SymbolVariable> getUsedSymbolVariables() {
        Collection<SymbolVariable> usedVariablesOnLocal =  new ArrayList<>();

        if (parameter.getValue() == null) {
            usedVariablesOnLocal.add(parameter);
        }

        if (result.getValue() == null) {
            usedVariablesOnLocal.add(result);
        }

        return  usedVariablesOnLocal;
    }
}
