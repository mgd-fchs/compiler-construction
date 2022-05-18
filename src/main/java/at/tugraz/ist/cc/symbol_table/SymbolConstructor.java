package at.tugraz.ist.cc.symbol_table;

import java.util.List;

public class SymbolConstructor extends SymbolCallable {

    public SymbolConstructor(SymbolClass symbolClass, List<SymbolVariable> params) {
        super(symbolClass.getClassName(), params, new SymbolVariable(
                SymbolType.CLASS, symbolClass, ""), symbolClass);
    }
}
