package at.tugraz.ist.cc.symbol_table;

public class SymbolVariable {
    private SymbolType type;
    private Object actualType;
    private String name;

    public SymbolVariable(SymbolType type, Object actualType, String name) {
        // TODO maybe change this kind of checks to asserts
        if (type == SymbolType.PRIMITIVE && !(actualType instanceof SymbolPrimitiveType) ||
            type == SymbolType.CLASS && !(actualType instanceof  SymbolClass)) {
            System.exit(-1);
        }

        this.type = type;
        this.actualType = actualType;
        this.name = name;
    }

    public SymbolType getType() {
        return type;
    }

    public Object getActualType() {
        return actualType;
    }
}
