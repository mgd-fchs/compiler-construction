package at.tugraz.ist.cc.symbol_table;

import java.util.*;

public class SymbolMethod extends SymbolCallable {
    public static final String MAIN_METHOD_NAME = "main";
    public final static Collection<SymbolMethod> IO_METHODS = new ArrayList<>();
    public final static String PRINT = "print";
    public final static String READ_INT = "readInt";
    public final static String READ_STRING = "readString";


    static {
        SymbolVariable returnValue = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "returnValue");

        SymbolVariable param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, PRINT, returnValue, List.of(param), null));

        param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, PRINT, returnValue, List.of(param), null));

        param = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "param");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, PRINT, returnValue, List.of(param), null));

        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, READ_INT, returnValue, new ArrayList<>(), null));

        returnValue = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "returnValue");
        IO_METHODS.add(new SymbolMethod(SymbolModifier.PUBLIC, READ_STRING, returnValue, new ArrayList<>(), null));
    }

    private final SymbolModifier accessSymbol;

    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue,
                        List<SymbolVariable> params, SymbolClass associatedClass) {
        super(name, params, returnValue, associatedClass);
        this.accessSymbol = accessSymbol;
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }
}
