package at.tugraz.ist.cc.symbol_table;

import java.util.*;

public class SymbolMethod extends SimpleCallable{
    public static final String MAIN_METHOD_NAME = "main";
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

    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue, List<SymbolVariable> params) {
        super(name, params, returnValue);
        this.accessSymbol = accessSymbol;
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }
}
