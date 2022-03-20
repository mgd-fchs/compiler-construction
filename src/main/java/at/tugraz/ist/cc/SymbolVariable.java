package at.tugraz.ist.cc;

public class SymbolVariable<T> {
    Type type;
    T value;
    String name;

    public SymbolVariable(Type type, T value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }
}
