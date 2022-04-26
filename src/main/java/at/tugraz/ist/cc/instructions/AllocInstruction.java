package at.tugraz.ist.cc.instructions;

import at.tugraz.ist.cc.CodeGeneratorUtils;
import at.tugraz.ist.cc.symbol_table.SimpleCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.Collection;
import java.util.Optional;

public class AllocInstruction extends BaseInstruction {
    private final SymbolVariable actualType;
    private final Collection<SymbolVariable> params;

    public AllocInstruction(SimpleCallable associatedCallable, SymbolVariable classType, Collection<SymbolVariable> params) {
        super(associatedCallable, Optional.of(classType));
        this.actualType = classType;
        this.params = params;
    }

    @Override
    public String buildAssemblyString() {
        String class_name = actualType.getTypeAsString();
        int resultLocalArrayIndex = associatedCallable.getLocalArrayIndexBySymbolVariable(result);
        // TODO loading params: could be done by derive from MethodInvocation...
        return String.format("" +
                        "  new %s                               ; allocate new memory for the object => saves obj-ref of the stack\n" +
                        "  dup                                  ; duplicates the obj-ref => now there are two obj-ref\n" +
                        "  invokespecial %s/<init>(%s)V         ; calls the constructor and pops a obj-ref from stack\n" +
                        "  astore %d                            ; pop obj-ref from stack and stores it into the right local space\n\n",
                class_name, class_name, CodeGeneratorUtils.getParameterTypesAsString(params), resultLocalArrayIndex);
    }
}
