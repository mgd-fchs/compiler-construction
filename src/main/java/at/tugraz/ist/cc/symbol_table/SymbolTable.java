package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.TypeCheckerJovaVisitorImpl;
import at.tugraz.ist.cc.error.ErrorHandler;
import org.antlr.v4.runtime.ParserRuleContext;

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

    public static void reset()
    {
        symbolTable = new SymbolTable();
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
            return TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DEFINITION_CLASS;
        }

        classes.add(symbolClass);
        return TypeCheckerJovaVisitorImpl.OK;
    }

    public Optional<SymbolClass> getClassByName(String name, ParserRuleContext ctx) {
        Optional<SymbolClass> found =  classes.stream().filter(
                element -> element.getClassName().equals(name)).findFirst();

        if (found.isEmpty()){
            ErrorHandler.INSTANCE.addUnknownTypeError(
                    ctx.start.getLine(), ctx.start.getCharPositionInLine(), name);
        }

        return found;
    }

    public Collection<SymbolClass> getClasses() {
        return classes;
    }

}
