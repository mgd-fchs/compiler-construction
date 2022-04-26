package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolType;

import java.util.HashMap;
import java.util.Map;

public enum OperatorTypes {
    ADD("+"), SUB("-"),
    MUL("*"), DIV("/"), MOD("%"),
    GREATER(">"), SMALLER("<"), GREATER_EQUAL(">="),
    SMALLER_EQUAL("<="), EQUAL("=="), UNEQUAL("!="),
    AND("&&"), OR("||"),
    NOT("!");

    private String value;
    private static Map strmap = new HashMap<>();

    OperatorTypes(String value) {
        this.value = value;
    }

    static {
        for (OperatorTypes type : OperatorTypes.values()) {
            strmap.put(type.value, type);
        }
    }

    public static OperatorTypes getOp(String type) {
        return (OperatorTypes) strmap.get(type);
    }
}
