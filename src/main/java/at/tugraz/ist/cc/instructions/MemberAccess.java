package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public class MemberAccess {
    public final SymbolVariable classRef;
    public final SymbolVariable memberRef;

    public MemberAccess(SymbolVariable classRef, SymbolVariable memberRef) {
        this.classRef = classRef;
        this.memberRef = memberRef;
    }

}
