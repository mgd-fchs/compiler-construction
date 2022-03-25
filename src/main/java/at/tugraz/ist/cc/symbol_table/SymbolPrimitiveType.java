package at.tugraz.ist.cc.symbol_table;

public enum SymbolPrimitiveType {
    BOOL, STRING, INT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
