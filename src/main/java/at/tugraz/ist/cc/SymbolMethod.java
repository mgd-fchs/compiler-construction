package at.tugraz.ist.cc;

import java.util.Collection;

public class SymbolMethod {
    private SymbolModifier accessSymbol;
    private String name;
    private Type returnValue;
    Collection<SymbolVariable> params;

    public SymbolMethod(SymbolModifier accessSymbol, String name, Type returnValue, Collection<SymbolVariable> params) {
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

    public Collection<SymbolVariable> getParams() {
        return params;
    }
}
