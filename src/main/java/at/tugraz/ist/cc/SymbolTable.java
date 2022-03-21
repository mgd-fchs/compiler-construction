package at.tugraz.ist.cc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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

    public SymbolClass getClassByName(String name) {
        Optional<SymbolClass>  found = classes.stream().filter(element -> element.getClassName().equals(name)).findFirst();
        return found.get(); // TODO check if exist => get lead otherwise to exception
    }
}
