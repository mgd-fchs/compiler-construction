package at.tugraz.ist.cc.symbol_table;

import java.util.List;

public class SymbolConstructor extends  SimpleCallable{

    public SymbolConstructor(String name, List<SymbolVariable> params) {
        super(name, params);
    }
}
