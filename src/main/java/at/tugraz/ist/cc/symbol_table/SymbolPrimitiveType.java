package at.tugraz.ist.cc.symbol_table;

import java.util.HashMap;
import java.util.Map;

public enum SymbolPrimitiveType {
    BOOL(1), STRING(2), INT(3), NIX(4);

    private int value;
    private static Map intmap = new HashMap<>();


    private SymbolPrimitiveType(int value) {
        this.value = value;

    }

    static {
        for (SymbolPrimitiveType type : SymbolPrimitiveType.values()) {
            intmap.put(type.value, type);
        }
    }

    public static SymbolPrimitiveType valueOf(int type) {
        return (SymbolPrimitiveType) intmap.get(type);
    }

    public int getValue() {
        return value;
    }

}
