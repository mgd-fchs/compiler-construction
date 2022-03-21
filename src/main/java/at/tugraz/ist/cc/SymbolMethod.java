package at.tugraz.ist.cc;

import java.util.Collection;

public class SymbolMethod {
    private SymbolModifier accessSymbol;
    private String name;
    private Type returnValue;
    // TODO refactor
    Collection<Object> params;
    // TODO add reference to return value if class

    public SymbolMethod(SymbolModifier accessSymbol, String name, Type returnValue, Collection<Object> params) {
        // TODO check if there are no params with same name
        this.accessSymbol = accessSymbol;
        this.name = name;
        this.returnValue = returnValue;
        this.params = params;
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }

    public String getName() {
        return name;
    }

    public Type getReturnValue() {
        return returnValue;
    }

    public Collection<Object> getParams() {
        return params;
    }
}
