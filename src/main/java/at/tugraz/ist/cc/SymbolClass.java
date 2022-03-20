package at.tugraz.ist.cc;

import java.util.ArrayList;
import java.util.Collection;

public class SymbolClass {
    Collection<SymbolVariable> member;
    Collection<String> methods;

    public SymbolClass() {
        this.member = new ArrayList<>();
        this.methods = new ArrayList<>();
    }


}
