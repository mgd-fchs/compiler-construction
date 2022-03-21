package at.tugraz.ist.cc;

import java.util.ArrayList;
import java.util.Collection;

public class SymbolTable {
    private Collection<SymbolClass> classes;

    public SymbolTable() {
        this.classes = new ArrayList<>();
    }


    public int addClass(SymbolClass symbolClass){
        boolean found = this.classes.contains(symbolClass);
        if (found) {
            return -1;
        }

        classes.add(symbolClass);
        return 0;
    }
}
