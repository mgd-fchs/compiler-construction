package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SymbolTable {
    private static SymbolTable symbolTable = null;
    public static SymbolTable getInstance()
    {
        if (symbolTable == null)
            symbolTable = new SymbolTable();

        return symbolTable;
    }

    private final Collection<SymbolClass> classes;

    private SymbolTable() {
        this.classes = new ArrayList<>();
    }


    public int addClass(SymbolClass symbolClass, JovaParser.Class_headContext ctx){
        boolean found = this.classes.contains(symbolClass);

        if (found) {
            ErrorHandler.INSTANCE.addClassDoubleDefError(
                    ctx.start.getLine(), ctx.start.getCharPositionInLine(), symbolClass.getClassName());
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
