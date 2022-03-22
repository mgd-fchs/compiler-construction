package at.tugraz.ist.cc.symbol_table;

import java.util.ArrayList;
import java.util.Collection;

public class SymbolMethod {
    private SymbolModifier accessSymbol;
    private String name;
    private SymbolType returnValue;
    // TODO refactor
    Collection<Object> params;
    // TODO add reference to return value if class

    private Collection<Object> localVariables;


    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolType returnValue, Collection<Object> params) {
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

    public SymbolType getReturnValue() {
        return returnValue;
    }

    public Collection<Object> getParams() {
        return params;
    }

    public void addVariable(Object obj)
    {
        localVariables.add(obj);
    }
}
