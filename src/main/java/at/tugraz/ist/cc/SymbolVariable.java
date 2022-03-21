package at.tugraz.ist.cc;

public class SymbolVariable<T> {
    PrimitveType type;
    T value;
    String name;

    public SymbolVariable(PrimitveType type, T value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }
}
