package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Optional;

public class MemberAccessInstruction extends BaseInstruction {
    public final SymbolVariable classRef;
    public final SymbolVariable memberRef;
    public SymbolVariable value;

    public MemberAccessInstruction(SimpleCallable associatedCallable, SymbolVariable classRef, SymbolVariable memberRef) {
        super(associatedCallable, Optional.of(memberRef));
        this.classRef = classRef;
        this.memberRef = memberRef;
        this.value = null;
    }

    @Override
    public String buildAssemblyString() {
        // TODO: if value != null -> put, else get
        return null;
    }

    public void setPutValue(SymbolVariable value){
        this.value = value;
    }

}
