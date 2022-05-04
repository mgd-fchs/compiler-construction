package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolTable;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author wehopeu
 *
 */
public class TypeChecker {

    private ParseTree parseTree;

    public int checkTypes(String file_path, boolean debug, boolean resetSymbolTable) {
        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();

        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, debug));
        parser.reset();
        parseTree = parser.program();

        if (ErrorHandler.INSTANCE.getNumParseErrors() != 0) {
            if (debug)
                ErrorHandler.INSTANCE.printErrorsAndWarnings();

            return ErrorHandler.INSTANCE.getNumParseErrors();
        }

        TypeCheckerJovaVisitorImpl checker = new TypeCheckerJovaVisitorImpl();
        checker.visit(parseTree);

        if (debug) {
            ErrorHandler.INSTANCE.printTypeErrors();
            ErrorHandler.INSTANCE.printTypeWarnings();
        }

        if (resetSymbolTable) {
            SymbolTable.reset();
        }

        return ErrorHandler.INSTANCE.getNumTypeErrors();
    }

    public int checkTypes(String file_path, boolean debug) {
        return checkTypes(file_path, debug, true);
    }

}
