package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author wehopeu
 *
 */
public class TypeChecker {

    public int checkTypes(String file_path, boolean debug) {
        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();

        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, debug));
        parser.reset();
        ParseTree parseTree = parser.program();

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

        if (ErrorHandler.INSTANCE.getNumTypeErrors() != 0) {
            return ErrorHandler.INSTANCE.getNumTypeErrors();
        }
        return 0;
    }

}
