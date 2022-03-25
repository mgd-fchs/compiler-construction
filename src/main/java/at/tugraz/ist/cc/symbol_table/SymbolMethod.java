package at.tugraz.ist.cc.symbol_table;

import java.util.List;

public class SymbolMethod extends SimpleCallable{
    private final SymbolModifier accessSymbol;
    private final SymbolVariable returnValue;


    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue, List<SymbolVariable> params) {
        super(name, params);
        // TODO check if there are no params with same name
        this.accessSymbol = accessSymbol;
        this.returnValue = returnValue;
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }

    public SymbolVariable getReturnValue() {
        return returnValue;
    }
    public Object getLocalVariableType(String id){
        Optional<SymbolVariable> found = localVariables.stream().filter(element -> element.getName().equals(id)).findFirst();
        return found.get().getActualType();
    }

    public SymbolVariable getLocalVariableById(String id){
        Optional<SymbolVariable> found = localVariables.stream().filter(element -> element.getName().equals(id)).findFirst();
        return found.get();
    }

}
