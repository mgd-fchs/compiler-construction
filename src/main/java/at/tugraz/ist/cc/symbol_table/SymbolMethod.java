package at.tugraz.ist.cc.symbol_table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SymbolMethod {
    private final SymbolModifier accessSymbol;
    private final String name;
    private final SymbolVariable returnValue;
    // TODO refactor
    private final Collection<Object> params;
    // TODO add reference to return value if class

    private final Collection<Object> localVariables;


    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue, Collection<Object> params) {
        // TODO check if there are no params with same name
        this.accessSymbol = accessSymbol;
        this.name = name;
        this.returnValue = returnValue;
        this.params = params;
        localVariables = new ArrayList<>();
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }

    public String getName() {
        return name;
    }

    public SymbolVariable getReturnValue() {
        return returnValue;
    }

    public Collection<Object> getParams() {
        return params;
    }

    public void addVariable(Object obj)
    {
        localVariables.add(obj);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolMethod that = (SymbolMethod) o;

        // TODO check if Objects.equals(params, that.params) really considers also the order of the elements
        // if a method is equal only the name an the params are relevant
        return Objects.equals(name, that.name) || Objects.equals(params, that.params);
    }
}
