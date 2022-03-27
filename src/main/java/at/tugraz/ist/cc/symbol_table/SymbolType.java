package at.tugraz.ist.cc.symbol_table;

import java.util.HashMap;
import java.util.Map;

public enum SymbolType {
    CLASS(31), PRIMITIVE(32);

    private int value;
    private static Map intmap = new HashMap<>();


    SymbolType(int value) {
        this.value = value;

    }

    static {
        for (SymbolType type : SymbolType.values()) {
            intmap.put(type.value, type);
        }
    }

    public static SymbolType valueOf(int type) {
        return (SymbolType) intmap.get(type);
    }

    public int getValue() {
        return value;
    }
}
