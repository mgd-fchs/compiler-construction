package at.tugraz.ist.cc.symbol_table;

public class SymbolVariable<T> {
    SymbolPrimitveType type;
    T value;
    String name;

    public SymbolVariable(SymbolPrimitveType type, T value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }
}
