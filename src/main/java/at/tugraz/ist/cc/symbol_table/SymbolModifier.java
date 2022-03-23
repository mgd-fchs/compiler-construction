package at.tugraz.ist.cc.symbol_table;

public enum SymbolModifier {
    PUBLIC, PRIVATE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
