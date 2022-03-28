package at.tugraz.ist.cc.symbol_table;

import java.util.*;

public class SymbolMethod extends SimpleCallable{
    public final static Collection<SymbolMethod> IO_METHODS = new ArrayList<>();


    static {
        SymbolVariable returnValue = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "returnValue");

        SymbolVariable param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, "print",  returnValue, List.of(param)));

        param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, "print",  returnValue, List.of(param)));

        param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, "print",  returnValue, List.of(param)));

        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, "readInt",  returnValue, new ArrayList<>()));

        returnValue = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "returnValue");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, "readString",  returnValue, new ArrayList<>()));
    }

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

    public SymbolVariable getMethodVariable(String name) {
        for (SymbolVariable v : params) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        for (SymbolVariable v : localVariables) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        return null;
    }

    public SymbolVariable getLocalVariableById(String id){
        Optional<SymbolVariable> found = localVariables.stream().filter(element -> element.getName().equals(id)).findFirst();
        if(found.isEmpty()){
            return null;
        }
        return found.get();
    }

}
