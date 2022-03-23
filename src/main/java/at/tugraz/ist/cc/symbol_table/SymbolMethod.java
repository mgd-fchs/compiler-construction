package at.tugraz.ist.cc.symbol_table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SymbolMethod {
    private final SymbolModifier accessSymbol;
    private final String name;
    private final SymbolVariable returnValue;
    // TODO refactor
    private final List<SymbolVariable> params;
    // TODO add reference to return value if class

    private final List<SymbolVariable> localVariables;


    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue, List<SymbolVariable> params) {
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

    public List<SymbolVariable> getParams() {
        return params;
    }

    public void addVariable(SymbolVariable symbolVariable)
    {
        localVariables.add(symbolVariable);
    }

    public SymbolVariable getMethodVariable(String name) {
        for (SymbolVariable v : params) {
            if (v.name.equals(name)) {
                return v;
            }
        }

        for (SymbolVariable v : localVariables) {
            if (v.name.equals(name)) {
                return v;
            }
        }

        return null;
    }

    /**
     * returns true if the objects are the same or if the names of the methods and also
     * the parameter order and types are the same. the return value of the method is not
     * taken into account
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolMethod that = (SymbolMethod) o;

        if (!Objects.equals(name, that.name)) return false;
        if (params.size() != that.params.size()) return false;

        for (int i = 0; i < params.size(); ++i) {
            if (params.get(i).type != that.params.get(i).type) {
                return false;
            }
        }

        return true;
    }
}
