package at.tugraz.ist.cc;

import java.util.ArrayList;
import java.util.Collection;

public class SymbolTable {
    private Collection<SymbolClass> classes;

    public SymbolTable() {
        this.classes = new ArrayList<>();
    }
}
